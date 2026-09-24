package cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class RocketProjectile extends AbstractArrow {
    public RocketProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        setNoGravity(true);
    }

    public RocketProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.ROCKET_PROJECTILE.get(), shooter, level);
        setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        explode();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        explode();
    }

    private void explode() {
        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0F, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected net.minecraft.world.item.ItemStack getPickupItem() { return net.minecraft.world.item.ItemStack.EMPTY; }
}