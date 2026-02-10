package cn.kuzuanpa.kubicdivers.stratagem.common;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KubicdiversStratagemMod.MOD_ID);

    public static final RegistryObject<EntityType<StratagemBallEntity>> STRATAGEM_BALL =
            ENTITIES.register("stratagem_ball",
                    () -> EntityType.Builder.<StratagemBallEntity>of(StratagemBallEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("stratagem_ball"));

    public static final RegistryObject<EntityType<BeaconEntity>> BEACON =
            ENTITIES.register("beacon",
                    () -> EntityType.Builder.<BeaconEntity>of(BeaconEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .fireImmune()
                            .build("beacon"));


    public static final RegistryObject<EntityType<MachineGunTurretEntity>> MACHINE_GUN_TURRET =
            ENTITIES.register("machine_gun_turret",
                    () -> EntityType.Builder.<MachineGunTurretEntity>of(MachineGunTurretEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.5F)
                            .clientTrackingRange(16)
                            .updateInterval(1)
                            .build("machine_gun_turret"));


    public static final RegistryObject<EntityType<APMineEntity>> AP_MINE =
            ENTITIES.register("ap_mine",
                    () -> EntityType.Builder.<APMineEntity>of(APMineEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.2F)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("ap_mine"));


    public static final RegistryObject<EntityType<HellpodEntity>> HELLPOD =
            ENTITIES.register("hellpod",
                    () -> EntityType.Builder.<HellpodEntity>of(HellpodEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(32)
                            .updateInterval(1)
                            .build("hellpod"));
}