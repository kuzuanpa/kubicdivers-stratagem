package cn.kuzuanpa.kubicdivers.stratagem.common.entity;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import cn.kuzuanpa.kubicdivers.stratagem.common.ModItems;
import cn.kuzuanpa.kubicdivers.stratagem.common.items.StratagemBallItem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.StratagemManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class StratagemBallEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<String> STRATAGEM_ID =
            SynchedEntityData.defineId(StratagemBallEntity.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Boolean> HAS_LANDED =
            SynchedEntityData.defineId(StratagemBallEntity.class, EntityDataSerializers.BOOLEAN);

    private Vec3 landingPos = null;

    public StratagemBallEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public StratagemBallEntity(Level level, LivingEntity shooter) {
        super(ModEntities.STRATAGEM_BALL.get(), shooter, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STRATAGEM_ID, "");
        this.entityData.define(HAS_LANDED, false);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.STRATAGEM_BALL.get();
    }

    public void setStratagemId(String stratagemId) {
        this.entityData.set(STRATAGEM_ID, stratagemId);
        KubicdiversStratagemMod.LOGGER.debug("Stratagem ball entity set with runningStratagem: {}", stratagemId);
    }

    @Override
    public void setItem(@NotNull ItemStack p_37447_) {
        super.setItem(p_37447_);
        setStratagemId(StratagemBallItem.getStratagemId( p_37447_));
    }

    public String getStratagemId() {
        return this.entityData.get(STRATAGEM_ID);
    }

    public IStratagem getStratagem() {
        return StratagemManager.getStratagem(getStratagemId());
    }

    public boolean hasLanded() {
        return this.entityData.get(HAS_LANDED);
    }

    public void setHasLanded(boolean landed) {
        this.entityData.set(HAS_LANDED, landed);
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide && !hasLanded()&&result.getType() == HitResult.Type.BLOCK) {
            setHasLanded(true);
            landingPos = result.getLocation();
            KubicdiversStratagemMod.LOGGER.info("Stratagem ball landed at {} with runningStratagem: {}", landingPos, getStratagemId());

            if (getStratagemId() != null && !getStratagemId().isEmpty()) {
                BeaconEntity beacon = new BeaconEntity(level(), landingPos, getStratagemId());
                beacon.setOwner(this.getOwner());
                level().addFreshEntity(beacon);

                KubicdiversStratagemMod.LOGGER.debug("Beacon entity created at {}", landingPos);
            }
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide && !hasLanded()) {
            Vec3 pos = this.position();
            this.level().addParticle(ParticleTypes.SMOKE,
                    pos.x + (this.random.nextDouble() - 0.5) * 0.1,
                    pos.y + 0.1,
                    pos.z + (this.random.nextDouble() - 0.5) * 0.1,
                    0, 0.02, 0);

            if (this.tickCount % 5 == 0) {
                this.level().addParticle(ParticleTypes.GLOW,
                        pos.x, pos.y, pos.z,
                        0, 0, 0);
            }
        }

        if (this.tickCount > 600) {
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("StratagemId", getStratagemId());
        nbt.putBoolean("HasLanded", hasLanded());
        if (landingPos != null) {
            nbt.putDouble("LandingPosX", landingPos.x);
            nbt.putDouble("LandingPosY", landingPos.y);
            nbt.putDouble("LandingPosZ", landingPos.z);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setStratagemId(nbt.getString("StratagemId"));
        setHasLanded(nbt.getBoolean("HasLanded"));
        if (nbt.contains("LandingPos")) {
            landingPos = new Vec3(nbt.getDouble("LandingPosX"),nbt.getDouble("LandingPosY"),nbt.getDouble("LandingPosZ"));
        }
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}