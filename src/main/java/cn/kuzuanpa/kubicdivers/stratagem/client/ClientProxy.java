package cn.kuzuanpa.kubicdivers.stratagem.client;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.client.renderer.*;
import cn.kuzuanpa.kubicdivers.stratagem.client.renderer.model.SentryRenderer;
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
        event.registerEntityRenderer(ModEntities.AP_MINE.get(), MineRenderer::new);
        event.registerEntityRenderer(ModEntities.GUN_SENTRY.get(), SentryRenderer::new);
        event.registerEntityRenderer(ModEntities.GATLING_SENTRY.get(), SentryRenderer::new);
        event.registerEntityRenderer(ModEntities.CANNON_SENTRY.get(), SentryRenderer::new);
        event.registerEntityRenderer(ModEntities.ROCKET_SENTRY.get(), SentryRenderer::new);
        event.registerEntityRenderer(ModEntities.EMS_MORTAR_SENTRY.get(), SentryRenderer::new);
        event.registerEntityRenderer(ModEntities.MORTAR_SENTRY.get(), SentryRenderer::new);

        event.registerEntityRenderer(ModEntities.ORBITAL_BULLET.get(), OrbitalBulletRenderer::new);
        event.registerEntityRenderer(ModEntities.SENTRY_BULLET.get(), SentryBulletRenderer::new);
        event.registerEntityRenderer(ModEntities.ROCKET_PROJECTILE.get(), SentryBulletRenderer::new);
        event.registerEntityRenderer(ModEntities.MORTAR_SHELL_EMS.get(), SentryBulletRenderer::new);
        event.registerEntityRenderer(ModEntities.MORTAR_SHELL.get(), SentryBulletRenderer::new);
    }
}
