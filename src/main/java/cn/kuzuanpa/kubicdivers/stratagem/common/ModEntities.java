package cn.kuzuanpa.kubicdivers.stratagem.common;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.*;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.*;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.*;
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
                            .updateInterval(4)
                            .build("hellpod"));

    // 注册加特林炮台
    public static final RegistryObject<EntityType<GunSentryEntity>> GUN_SENTRY =
            ENTITIES.register("gun_sentry", () -> EntityType.Builder.<GunSentryEntity>of(GunSentryEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.5F)
                    .build("gun_sentry"));

    public static final RegistryObject<EntityType<GatlingSentryEntity>> GATLING_SENTRY =
            ENTITIES.register("gatling_sentry", () -> EntityType.Builder.<GatlingSentryEntity>of(GatlingSentryEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.5F)
                    .build("gatling_sentry"));

    // 注册子弹
    public static final RegistryObject<EntityType<SentryBullet>> SENTRY_BULLET =
            ENTITIES.register("sentry_bullet", () -> EntityType.Builder.<SentryBullet>of(SentryBullet::new, MobCategory.MISC)
                    .sized(0.2F, 0.2F)
                    .clientTrackingRange(16)
                    .updateInterval(1)
                    .build("sentry_bullet"));

    // 注册子弹
    public static final RegistryObject<EntityType<OrbitalBullet>> ORBITAL_BULLET =
            ENTITIES.register("orbital_bullet", () -> EntityType.Builder.<OrbitalBullet>of(OrbitalBullet::new, MobCategory.MISC)
                    .sized(1F, 1F)
                    .clientTrackingRange(32)
                    .updateInterval(1)
                    .build("orbital_bullet"));
    public static final RegistryObject<EntityType<RocketProjectile>> ROCKET_PROJECTILE =
            ENTITIES.register("rocket_projectile", () -> EntityType.Builder.<RocketProjectile>of(RocketProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build("rocket_projectile"));

    public static final RegistryObject<EntityType<MortarShellProjectile>> MORTAR_SHELL =
            ENTITIES.register("mortar_shell", () -> EntityType.Builder.<MortarShellProjectile>of(MortarShellProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build("mortar_shell"));

    public static final RegistryObject<EntityType<MortarEMSProjectile>> MORTAR_SHELL_EMS =
            ENTITIES.register("mortar_shell_ems", () -> EntityType.Builder.<MortarEMSProjectile>of(MortarEMSProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build("mortar_shell_ems"));

    public static final RegistryObject<EntityType<CannonSentryEntity>> CANNON_SENTRY =
            ENTITIES.register("cannon_sentry", () -> EntityType.Builder.<CannonSentryEntity>of(CannonSentryEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.5F).build("cannon_sentry"));

    public static final RegistryObject<EntityType<RocketSentryEntity>> ROCKET_SENTRY =
            ENTITIES.register("rocket_sentry", () -> EntityType.Builder.<RocketSentryEntity>of(RocketSentryEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.5F).build("rocket_sentry"));

    public static final RegistryObject<EntityType<MortarSentryEntity>> MORTAR_SENTRY =
            ENTITIES.register("mortar_sentry", () -> EntityType.Builder.<MortarSentryEntity>of(MortarSentryEntity::new, MobCategory.MISC)
                    .sized(1.2F, 1.2F).build("mortar_sentry"));

    public static final RegistryObject<EntityType<EMSMortarSentryEntity>> EMS_MORTAR_SENTRY =
            ENTITIES.register("ems_mortar_sentry", () -> EntityType.Builder.<EMSMortarSentryEntity>of(EMSMortarSentryEntity::new, MobCategory.MISC)
                    .sized(1.2F, 1.2F).build("ems_mortar_sentry"));
}