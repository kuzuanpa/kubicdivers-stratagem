package cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.SentryBullet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class GatlingSentryEntity extends AbstractSentryEntity {
    public GatlingSentryEntity(EntityType<? extends GatlingSentryEntity> type, Level level) {
        super(type, level);
    }

    public GatlingSentryEntity(Level level) {
        super(ModEntities.GATLING_SENTRY.get(), level);
    }


    @Override protected int getFireRate() { return 2; }
    @Override protected float getRange() { return 24.0F; }

    @Override protected SoundEvent getShootSound() { return SoundEvents.STONE_PLACE; }

    @Override
    protected void performAttack(LivingEntity target) {
        SentryBullet bullet = new SentryBullet(this.level(), this);
        bullet.setPos(bullet.position().add(0,-0.05,0));
        double d0 = target.getX() - this.getX();
        double d1 = target.getEyeY() - bullet.getY();
        double d2 = target.getZ() - this.getZ();

        bullet.shoot(d0, d1 , d2, 3.0F, 1.5F);
        this.level().addFreshEntity(bullet);
    }
}