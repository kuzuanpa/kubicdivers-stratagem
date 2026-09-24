package cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class MortarShellProjectile extends AbstractArrow {
    public MortarShellProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }
    public MortarShellProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.MORTAR_SHELL.get(), shooter, level);
    }
    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.getDeltaMovement().y < -1) {
            if (this.tickCount % 20 == 0) {
                this.playSound(SoundEvents.GHAST_SCREAM, 1.5F, 2.0F);
            }
        }
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 0, 0.05, 0);
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
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 4.0F, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected net.minecraft.world.item.ItemStack getPickupItem() { return net.minecraft.world.item.ItemStack.EMPTY; }
}