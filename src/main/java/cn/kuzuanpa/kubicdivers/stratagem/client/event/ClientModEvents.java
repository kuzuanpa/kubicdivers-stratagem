package cn.kuzuanpa.kubicdivers.stratagem.client.event;

import cn.kuzuanpa.kubicdivers.stratagem.client.ClientLayerRegistry;
import cn.kuzuanpa.kubicdivers.stratagem.client.renderer.model.SentryModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
                event.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "hellpod-main"));
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // 注册模型几何体
        event.registerLayerDefinition(ClientLayerRegistry.SENTRY_LAYER, SentryModel::createBodyLayer);
    }
}
