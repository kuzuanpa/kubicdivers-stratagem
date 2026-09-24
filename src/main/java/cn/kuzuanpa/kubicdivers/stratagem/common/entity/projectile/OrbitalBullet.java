package cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile;
import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class OrbitalBullet extends AbstractArrow {
    Function<OrbitalBullet, Boolean> onHitCallback;
    float damage;
    public OrbitalBullet(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public OrbitalBullet(Level level, LivingEntity shooter, float damage) {
        super(ModEntities.ORBITAL_BULLET.get(), shooter, level);
        this.damage=damage;
    }

    public OrbitalBullet setCallback(Function<OrbitalBullet, Boolean> onHitCallback){
        this.onHitCallback = onHitCallback;
        return this;
    }
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }
    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity target) {
            target.hurt(this.damageSources().generic(), this.damage);
        }
        detonate();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_36755_) {
        detonate();
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void detonate() {
        if(onHitCallback==null || onHitCallback.apply(this))this.discard();
    }
}