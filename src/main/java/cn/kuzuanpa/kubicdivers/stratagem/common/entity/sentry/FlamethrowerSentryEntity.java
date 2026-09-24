package cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FlamethrowerSentryEntity extends AbstractSentryEntity {
    public FlamethrowerSentryEntity(EntityType<? extends FlamethrowerSentryEntity> type, Level level) {
        super(type, level);
    }

    @Override protected int getFireRate() { return 1; } // 每tick都喷
    @Override protected float getRange() { return 10.0F; } // 射程短
    @Override protected SoundEvent getShootSound() { return SoundEvents.FIRECHARGE_USE; }

    @Override
    protected void performAttack(LivingEntity target) {
        // 1. 生成火焰粒子束 (向目标方向)
        double dx = target.getX() - this.getX();
        double dy = (target.getY() + target.getEyeHeight()/2) - this.getEyeY();
        double dz = target.getZ() - this.getZ();

        // 简单的粒子生成逻辑，实际可以用向量计算更精确的圆锥体
        for(int i=0; i<5; i++) {
            this.level().addParticle(net.minecraft.core.particles.ParticleTypes.FLAME,
                    this.getX(), this.getEyeY(), this.getZ(),
                    dx * 0.1 + (random.nextDouble()-0.5)*0.2,
                    dy * 0.1 + (random.nextDouble()-0.5)*0.2,
                    dz * 0.1 + (random.nextDouble()-0.5)*0.2
            );
        }

        // 2. 直接造成伤害和燃烧 (AOE或单体)
        target.setSecondsOnFire(5);
        target.hurt(this.damageSources().mobAttack(this), 2.0F);
    }
}