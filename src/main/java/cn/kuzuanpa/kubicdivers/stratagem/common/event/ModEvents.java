package cn.kuzuanpa.kubicdivers.stratagem.common.event;


import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GUN_SENTRY.get(), GunSentryEntity.createAttributes().build());
        event.put(ModEntities.GATLING_SENTRY.get(), GatlingSentryEntity.createAttributes().build());
        event.put(ModEntities.CANNON_SENTRY.get(), CannonSentryEntity.createAttributes().build());
        event.put(ModEntities.ROCKET_SENTRY.get(), RocketSentryEntity.createAttributes().build());
        event.put(ModEntities.MORTAR_SENTRY.get(), MortarSentryEntity.createAttributes().build());
        event.put(ModEntities.EMS_MORTAR_SENTRY.get(), EMSMortarSentryEntity.createAttributes().build());
    }
}