package cn.kuzuanpa.kubicdivers.stratagem.common;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.network.CooldownSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = KubicdiversStratagemMod.MOD_ID)
public class PlayerCooldownManager {
    private static final Map<UUID, Map<String, CooldownEntry>> playerCooldowns = new ConcurrentHashMap<>();

    private record CooldownEntry(long endTime, int duration, IStratagem stratagem) {
        public boolean isExpired() {
            return System.currentTimeMillis() >= endTime;
        }

        public float getRemainingRatio() {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= endTime) return 0.0f;
            return (float) (endTime - currentTime) / duration;
        }

        public long getRemainingMillis() {
            return Math.max(0, endTime - System.currentTimeMillis());
        }
    }

    public static void startCooldown(Player player, IStratagem stratagem) {
        if (player == null || stratagem == null) return;

        UUID playerId = player.getUUID();
        String stratagemId = stratagem.getId();
        int cooldownTicks = stratagem.getCooldown();

        if (cooldownTicks <= 0) return;

        long cooldownMillis = cooldownTicks * 50L;
        long endTime = System.currentTimeMillis() + cooldownMillis;

        CooldownEntry entry = new CooldownEntry(endTime, (int)cooldownMillis, stratagem);

        playerCooldowns.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
                .put(stratagemId, entry);

        KubicdiversStratagemMod.LOGGER.debug("Started cooldown for {}: {} ({} ticks)",
                player.getName().getString(), stratagem.getName(), cooldownTicks);
    }

    public static boolean isOnCooldown(Player player, IStratagem stratagem) {
        if (player == null || stratagem == null) return false;

        CooldownEntry entry = getCooldownEntry(player, stratagem);
        return entry != null && !entry.isExpired();
    }

    public static boolean isOnCooldown(Player player, String stratagemId) {
        if (player == null || stratagemId == null) return false;

        Map<String, CooldownEntry> cooldowns = playerCooldowns.get(player.getUUID());
        if (cooldowns == null) return false;

        CooldownEntry entry = cooldowns.get(stratagemId);
        return entry != null && !entry.isExpired();
    }

    public static long getRemainingCooldownMillis(Player player, IStratagem stratagem) {
        CooldownEntry entry = getCooldownEntry(player, stratagem);
        return entry != null ? entry.getRemainingMillis() : 0;
    }

    public static float getRemainingCooldownRatio(Player player, IStratagem stratagem) {
        CooldownEntry entry = getCooldownEntry(player, stratagem);
        return entry != null ? entry.getRemainingRatio() : 0.0f;
    }

    public static int getRemainingCooldownTicks(Player player, IStratagem stratagem) {
        return (int)(getRemainingCooldownMillis(player, stratagem) / 50L);
    }

    public static List<IStratagem> getCooldownStratagems(Player player) {
        List<IStratagem> result = new ArrayList<>();
        if (player == null) return result;

        Map<String, CooldownEntry> cooldowns = playerCooldowns.get(player.getUUID());
        if (cooldowns == null) return result;

        for (CooldownEntry entry : cooldowns.values()) {
            if (!entry.isExpired()) {
                result.add(entry.stratagem());
            }
        }

        return result;
    }

    public static void reduceCooldown(Player player, IStratagem stratagem, int reduceTicks) {
        CooldownEntry entry = getCooldownEntry(player, stratagem);
        if (entry != null && !entry.isExpired()) {
            long newEndTime = entry.endTime - (reduceTicks * 50L);
            if (newEndTime <= System.currentTimeMillis()) {
                removeCooldown(player, stratagem);
            } else {
                CooldownEntry newEntry = new CooldownEntry(
                        newEndTime,
                        entry.duration,
                        entry.stratagem
                );

                playerCooldowns.get(player.getUUID())
                        .put(stratagem.getId(), newEntry);
            }
        }
    }

    public static void resetAllCooldowns(Player player) {
        if (player != null) {
            playerCooldowns.remove(player.getUUID());
            KubicdiversStratagemMod.LOGGER.debug("Reset all cooldowns for {}",
                    player.getName().getString());
        }
    }

    public static void removeCooldown(Player player, IStratagem stratagem) {
        if (player != null && stratagem != null) {
            Map<String, CooldownEntry> cooldowns = playerCooldowns.get(player.getUUID());
            if (cooldowns != null) {
                cooldowns.remove(stratagem.getId());
            }
        }
    }

    private static CooldownEntry getCooldownEntry(Player player, IStratagem stratagem) {
        if (player == null || stratagem == null) return null;

        Map<String, CooldownEntry> cooldowns = playerCooldowns.get(player.getUUID());
        if (cooldowns == null) return null;

        CooldownEntry entry = cooldowns.get(stratagem.getId());
        if (entry == null || entry.isExpired()) {
            cooldowns.remove(stratagem.getId());
            return null;
        }

        return entry;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.getServer().getTickCount() % 200 == 0) {
                cleanupExpiredCooldowns();
            }
        }
    }

    private static void cleanupExpiredCooldowns() {
        int cleaned = 0;
        for (Map<String, CooldownEntry> cooldowns : playerCooldowns.values()) {
            Iterator<Map.Entry<String, CooldownEntry>> iterator = cooldowns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, CooldownEntry> entry = iterator.next();
                if (entry.getValue().isExpired()) {
                    iterator.remove();
                    cleaned++;
                }
            }
        }

        if (cleaned > 0) {
            KubicdiversStratagemMod.LOGGER.debug("Cleaned up {} expired cooldowns", cleaned);
        }
    }

    public static void syncCooldownToClient(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            KubicdiversStratagemMod.NETWORK_CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new CooldownSyncPacket(player)
            );
        }
    }
}