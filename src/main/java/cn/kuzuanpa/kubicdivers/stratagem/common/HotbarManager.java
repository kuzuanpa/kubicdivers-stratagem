package cn.kuzuanpa.kubicdivers.stratagem.common;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.items.StratagemBallItem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.network.SetStratagemBallPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = KubicdiversStratagemMod.MOD_ID)
public class HotbarManager {
    private static final Map<UUID, HotbarState> playerStates = new HashMap<>();
    private static final int BALL_SLOT_INDEX = 8;

    private static class HotbarState {
        ItemStack originalItem = ItemStack.EMPTY;
        ItemStack stratagemBall = ItemStack.EMPTY;
        int originalSlot = 0;
        boolean isActive = false;
        IStratagem selectedStratagem = null;
        long activationTime = 0;

        public void reset() {
            originalItem = ItemStack.EMPTY;
            stratagemBall = ItemStack.EMPTY;
            originalSlot = 0;
            isActive = false;
            selectedStratagem = null;
            activationTime = 0;
        }
    }

    public static void moveStratagemSlot(Player player){
        UUID playerId = player.getUUID();
        HotbarState state = playerStates.computeIfAbsent(playerId, k -> new HotbarState());

        if (state.isActive) return;

        Inventory inventory = player.getInventory();
        state.originalSlot = inventory.selected;

        inventory.selected = BALL_SLOT_INDEX;

        state.isActive = true;
        state.activationTime = System.currentTimeMillis();

        KubicdiversStratagemMod.LOGGER.debug("Stratagem mode activated for player {}", player.getName().getString());
    }
    public static void replaceStratagemBall(Player player, IStratagem stratagem) {
        UUID playerId = player.getUUID();
        HotbarState state = playerStates.computeIfAbsent(playerId, k -> new HotbarState());

        ItemStack ball = new ItemStack(ModItems.STRATAGEM_BALL.get());
        if (stratagem == null) return;

        StratagemBallItem.setStratagem(ball, stratagem);
        state.selectedStratagem = stratagem;

        state.stratagemBall = ball;
        KubicdiversStratagemMod.NETWORK_CHANNEL.sendToServer(new SetStratagemBallPacket(stratagem.getId(), true));
    }

    public static void deactivateStratagemMode(Player player) {
        UUID playerId = player.getUUID();
        HotbarState state = playerStates.get(playerId);

        if (state == null || !state.isActive) return;

        Inventory inventory = player.getInventory();

        ItemStack ballSlotItem = inventory.getItem(BALL_SLOT_INDEX);
        if (ballSlotItem.isEmpty()) {
            if (state.originalSlot >= 0 && state.originalSlot < 9) inventory.selected = state.originalSlot;
        }

        KubicdiversStratagemMod.NETWORK_CHANNEL.sendToServer(new SetStratagemBallPacket("", false));

        state.reset();
        playerStates.remove(playerId);

        KubicdiversStratagemMod.LOGGER.debug("Stratagem mode deactivated for player {}", player.getName().getString());
    }

    public static void forceDeactivate(Player player) {
        UUID playerId = player.getUUID();
        playerStates.remove(playerId);
    }

    public static boolean isPlayerInStratagemMode(Player player) {
        HotbarState state = playerStates.get(player.getUUID());
        return state != null && state.isActive;
    }
    public static IStratagem getSelectedStratagem(Player player) {
        HotbarState state = playerStates.get(player.getUUID());
        return state != null ? state.selectedStratagem : null;
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        UUID playerId = player.getUUID();
        HotbarState state = playerStates.get(playerId);

        if (state != null && state.isActive) {
            if (System.currentTimeMillis() - state.activationTime > 30000) {
                forceDeactivate(player);
            }
        }
    }
}