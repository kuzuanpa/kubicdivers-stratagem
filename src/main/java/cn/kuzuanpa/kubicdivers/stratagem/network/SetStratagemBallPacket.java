package cn.kuzuanpa.kubicdivers.stratagem.network;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.ModItems;
import cn.kuzuanpa.kubicdivers.stratagem.common.items.StratagemBallItem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.StratagemManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetStratagemBallPacket {
    private final String stratagemId;
    private final boolean activate;

    public SetStratagemBallPacket(String stratagemId, boolean activate) {
        this.stratagemId = stratagemId;
        this.activate = activate;
    }

    public SetStratagemBallPacket(String stratagemId) {
        this.stratagemId = stratagemId;
        this.activate = true;
    }

    public static void encode(SetStratagemBallPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.stratagemId);
        buffer.writeBoolean(packet.activate);
    }

    public static SetStratagemBallPacket decode(FriendlyByteBuf buffer) {
        return new SetStratagemBallPacket(buffer.readUtf(), buffer.readBoolean());
    }

    public static void handle(SetStratagemBallPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();

            if (player != null) {
                handleServerSide(player, packet.stratagemId, packet.activate);
                context.get().setPacketHandled(true);
            }
        });
    }

    private static void handleServerSide(ServerPlayer player, String stratagemId, boolean activate) {
        if (!activate) {
            removeSlot(player);
        } else {
            setStratagemBallSlot(player, stratagemId);
        }
    }

    private static void setStratagemBallSlot(ServerPlayer player, String stratagemId) {
        Inventory inventory = player.getInventory();
        int ballSlot = 8;
        ItemStack ball = new ItemStack(ModItems.STRATAGEM_BALL.get());
        StratagemBallItem.setStratagem(ball, StratagemManager.getStratagem(stratagemId));

        inventory.setItem(ballSlot, ball);

        KubicdiversStratagemMod.LOGGER.debug("Server set stratagem ball for player {} in slot {}",
                player.getName().getString(), ballSlot);
    }

    private static void removeSlot(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        int ballSlot = 8;
        inventory.removeItem(ballSlot, 64);

    }
}