package cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry;

import cn.kuzuanpa.kubicdivers.common.event.KubicEntityHelper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;

public abstract class AbstractSentryEntity extends PathfinderMob {
    private int attackCooldown = 0;

    private static final EntityDataAccessor<Boolean> IS_FIRING = SynchedEntityData.defineId(AbstractSentryEntity.class, EntityDataSerializers.BOOLEAN);

    protected AbstractSentryEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    protected abstract int getFireRate();
    protected abstract float getRange();
    protected abstract void performAttack(LivingEntity target);
    protected abstract SoundEvent getShootSound();

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_FIRING, false);
    }

    public boolean isFiring() {
        return this.entityData.get(IS_FIRING);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        LivingEntity target = this.getTarget();

        if (target == null || !target.isAlive() || this.distanceTo(target) > getRange()) {
            this.entityData.set(IS_FIRING, false);
            return;
        }

        this.lookAt(target, 20.0F, 20.0F);

        if (this.tickCount % getFireRate() == 0 && isLookingAtTarget(target) && getHealth() > 0) {
            this.entityData.set(IS_FIRING, true);
            this.performAttack(target);
            this.playSound(getShootSound(), 1.0F, 1.0F);
        } else {
            if (getFireRate() > 10) this.entityData.set(IS_FIRING, false);
        }
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false,
                (e) -> KubicEntityHelper.getKubicEntity(e) != null));
    }
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) return;

        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
            this.yHeadRot = this.getYRot();
            this.yBodyRot = this.getYRot();
        }
    }
    private boolean isLookingAtTarget(LivingEntity target) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        float targetYaw = (float) (Mth.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
        return Math.abs(Mth.wrapDegrees(this.getYRot() - targetYaw)) < 3.0F;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D) // 炮台不能动
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D) // 不受击退
                .add(Attributes.FOLLOW_RANGE, 32.0D); // 索敌范围
    }

    @Override
    public boolean isPushable() { return false; }
}
