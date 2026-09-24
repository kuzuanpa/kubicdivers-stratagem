package cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class MortarEMSProjectile extends AbstractArrow {
    public MortarEMSProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }
    public MortarEMSProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.MORTAR_SHELL_EMS.get(), shooter, level);
    }
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), 0, 0.05, 0);
        }
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        detonate();
    }
    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        detonate();
    }
    private void detonate() {
        if (!this.level().isClientSide) {
                AreaEffectCloud cloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
                cloud.setRadius(5.0F);
                cloud.setWaitTime(0);
                cloud.setDuration(100);
                cloud.setParticle(ParticleTypes.ELECTRIC_SPARK);

                cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 4));
                cloud.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1));

                this.level().addFreshEntity(cloud);

                this.playSound(net.minecraft.sounds.SoundEvents.TRIDENT_THUNDER, 1.0F, 2.0F);
            this.discard();
        }
    }

    @Override
    protected net.minecraft.world.item.ItemStack getPickupItem() { return net.minecraft.world.item.ItemStack.EMPTY; }
}