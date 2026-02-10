package cn.kuzuanpa.kubicdivers.stratagem.client;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.client.renderer.*;
import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = KubicdiversStratagemMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.STRATAGEM_BALL.get(), StratagemBallRenderer::new);
        event.registerEntityRenderer(ModEntities.BEACON.get(), BeaconRenderer::new);
        event.registerEntityRenderer(ModEntities.HELLPOD.get(), HellpodRenderer::new);
        event.registerEntityRenderer(ModEntities.MACHINE_GUN_TURRET.get(), TurretRenderer::new);
        event.registerEntityRenderer(ModEntities.AP_MINE.get(), MineRenderer::new);
    }
}
