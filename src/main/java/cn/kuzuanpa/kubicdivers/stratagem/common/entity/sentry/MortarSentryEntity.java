package cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.MortarShellProjectile;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

public class MortarSentryEntity extends AbstractSentryEntity {
    public MortarSentryEntity(EntityType<? extends MortarSentryEntity> type, Level level) {
        super(type, level);
    }

    public MortarSentryEntity(Level level) {
        super(ModEntities.MORTAR_SENTRY.get(), level);
    }
    @Override protected int getFireRate() { return 80; }
    @Override protected float getRange() { return 48.0F; }
    @Override protected SoundEvent getShootSound() { return SoundEvents.GENERIC_EXPLODE; }

    @Override
    protected void performAttack(LivingEntity target) {
        AbstractArrow shell = createBullet(target);
        launchMortarShell(shell, target);
    }

    protected AbstractArrow createBullet(LivingEntity target){
        return new MortarShellProjectile(this.level(), this);
    }

    protected void launchMortarShell(AbstractArrow shell, LivingEntity target) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);

        double vY = 1.2D + (horizontalDist * 0.05D);

        double estimatedTicks = (vY * 2.0D) / 0.08D;

        double vH = horizontalDist / estimatedTicks;

        double yaw = Math.atan2(dz, dx);
        double vx = Math.cos(yaw) * vH;
        double vz = Math.sin(yaw) * vH;

        shell.shoot(dx, vY * horizontalDist * 0.1, dz, (float)vH * 6.0F, 0.2F);
        shell.setDeltaMovement(vx, vY, vz);

        this.level().addFreshEntity(shell);
    }
}