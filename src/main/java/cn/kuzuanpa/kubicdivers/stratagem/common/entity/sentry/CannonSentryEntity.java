package cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.SentryBullet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class CannonSentryEntity extends AbstractSentryEntity {
    public CannonSentryEntity(EntityType<? extends CannonSentryEntity> type, Level level) {
        super(type, level);
    }

    public CannonSentryEntity(Level level) {
        super(ModEntities.CANNON_SENTRY.get(), level);
    }
    @Override protected int getFireRate() { return 40; }
    @Override protected float getRange() { return 32.0F; }
    @Override protected SoundEvent getShootSound() { return SoundEvents.GENERIC_EXPLODE; }

    @Override
    protected void performAttack(LivingEntity target) {
        SentryBullet bullet = new SentryBullet(this.level(), this);
        double d0 = target.getX() - this.getX();
        double d1 = target.getEyeY() - bullet.getY();
        double d2 = target.getZ() - this.getZ();

        bullet.shoot(d0, d1  , d2, 5.0F, 1.2F);
        this.level().addFreshEntity(bullet);
    }
}