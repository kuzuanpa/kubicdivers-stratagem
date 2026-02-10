package cn.kuzuanpa.kubicdivers.stratagem.client.event;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KubicdiversStratagemMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
                event.register(ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "hellpod-main"));
    }
}
