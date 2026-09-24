package cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.RocketProjectile;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class RocketSentryEntity extends AbstractSentryEntity {
    public RocketSentryEntity(EntityType<? extends RocketSentryEntity> type, Level level) {
        super(type, level);
    }

    public RocketSentryEntity(Level level) {
        super(ModEntities.ROCKET_SENTRY.get(), level);
    }
    @Override protected int getFireRate() { return 50; }
    @Override protected float getRange() { return 32.0F; }
    @Override protected SoundEvent getShootSound() { return SoundEvents.FIREWORK_ROCKET_LAUNCH; }

    @Override
    protected void performAttack(LivingEntity target) {
        RocketProjectile rocket = new RocketProjectile(this.level(), this);
        double d0 = target.getX() - this.getX();
        double d1 = target.getEyeY() - rocket.getY();
        double d2 = target.getZ() - this.getZ();

        rocket.shoot(d0, d1, d2, 2F, 0.5F);
        this.level().addFreshEntity(rocket);
    }
}