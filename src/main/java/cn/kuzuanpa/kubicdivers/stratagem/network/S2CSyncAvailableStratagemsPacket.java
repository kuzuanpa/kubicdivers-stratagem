package cn.kuzuanpa.kubicdivers.stratagem.network;

import cn.kuzuanpa.kubicdivers.stratagem.client.StratagemInputHandler;
import cn.kuzuanpa.kubicdivers.stratagem.common.StratagemPlayerManager;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.StratagemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class S2CSyncAvailableStratagemsPacket {
    private final List<String> stratagemIds;

    public S2CSyncAvailableStratagemsPacket(List<String> ids) {
        this.stratagemIds = ids;
    }

    public static void encode(S2CSyncAvailableStratagemsPacket msg, FriendlyByteBuf buf) {
        buf.writeCollection(msg.stratagemIds, FriendlyByteBuf::writeUtf);
    }

    public static S2CSyncAvailableStratagemsPacket decode(FriendlyByteBuf buf) {
        return new S2CSyncAvailableStratagemsPacket(
                buf.readCollection(ArrayList::new, FriendlyByteBuf::readUtf)
        );
    }

    public static void handle(S2CSyncAvailableStratagemsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                List<IStratagem> stratagems = msg.stratagemIds.stream()
                        .map(StratagemManager::getStratagem)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                StratagemPlayerManager.setAvailStratagems(player, stratagems);

                StratagemInputHandler.getInstance().refreshAvailableStratagems();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}