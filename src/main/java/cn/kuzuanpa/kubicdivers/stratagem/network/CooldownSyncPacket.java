package cn.kuzuanpa.kubicdivers.stratagem.network;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.PlayerCooldownManager;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CooldownSyncPacket {
    private final List<String> cooldownStratagemIds;
    private final List<Long> cooldownEndTimes;

    public CooldownSyncPacket() {
        this.cooldownStratagemIds = new ArrayList<>();
        this.cooldownEndTimes = new ArrayList<>();
    }

    public CooldownSyncPacket(Player player) {
        this.cooldownStratagemIds = new ArrayList<>();
        this.cooldownEndTimes = new ArrayList<>();

        List<IStratagem> cooldownStratagems = PlayerCooldownManager.getCooldownStratagems(player);
        for (IStratagem stratagem : cooldownStratagems) {
            long remaining = PlayerCooldownManager.getRemainingCooldownMillis(player, stratagem);
            if (remaining > 0) {
                cooldownStratagemIds.add(stratagem.getId());
                cooldownEndTimes.add(System.currentTimeMillis() + remaining);
            }
        }
    }

    public static void encode(CooldownSyncPacket packet, FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();

        ListTag idList = new ListTag();
        for (String id : packet.cooldownStratagemIds) {
            idList.add(StringTag.valueOf(id));
        }
        tag.put("Ids", idList);

        ListTag timeList = new ListTag();
        for (Long time : packet.cooldownEndTimes) {
            timeList.add(StringTag.valueOf(time.toString()));
        }
        tag.put("Times", timeList);

        buffer.writeNbt(tag);
    }

    public static CooldownSyncPacket decode(FriendlyByteBuf buffer) {
        CooldownSyncPacket packet = new CooldownSyncPacket();
        CompoundTag tag = buffer.readNbt();

        if (tag != null) {
            ListTag idList = tag.getList("Ids", Tag.TAG_STRING);
            ListTag timeList = tag.getList("Times", Tag.TAG_STRING);

            for (int i = 0; i < idList.size(); i++) {
                packet.cooldownStratagemIds.add(idList.getString(i));
            }

            for (int i = 0; i < timeList.size(); i++) {
                packet.cooldownEndTimes.add(Long.parseLong(timeList.getString(i)));
            }
        }

        return packet;
    }

    public static void handle(CooldownSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (context.get().getDirection().getReceptionSide().isClient()) {
                handleClient(packet);
            }
        });
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(CooldownSyncPacket packet) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;


        KubicdiversStratagemMod.LOGGER.debug("Received cooldown sync for {} stratagems",
                packet.cooldownStratagemIds.size());
    }
}