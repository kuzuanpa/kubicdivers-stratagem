package cn.kuzuanpa.kubicdivers.stratagem.common;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.StratagemManager;
import cn.kuzuanpa.kubicdivers.stratagem.network.S2CSyncAvailableStratagemsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class StratagemPlayerManager {
    public static HashMap<UUID, List<IStratagem>> map = new HashMap<>();
    public static List<IStratagem> getAvailStratagem(Player player){
        if(player == null)return List.of();

        List<IStratagem> list = map.get(player.getUUID());
        if (list == null)return List.of();
        return list;
    }
    public static List<IStratagem> getUnlockedStratagem(Player player){
        return StratagemManager.getAllStratagems().stream().toList();
    }
    public static List<String> getAvailStratagemID(Player player){
        if(player == null)return List.of();
        return getAvailStratagem(player).stream().filter(Objects::nonNull).map(IStratagem::getId).collect(Collectors.toList());
    }
    public static void setAvailStratagems(Player player, List<IStratagem> list){
        map.put(player.getUUID(), list);
    }
    public static void syncAvailStratagem(ServerPlayer player){
        List<String> list =StratagemPlayerManager.getAvailStratagemID(player);
        if(list.isEmpty())return;

        KubicdiversStratagemMod.NETWORK_CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new S2CSyncAvailableStratagemsPacket(list)
        );
    }
}
