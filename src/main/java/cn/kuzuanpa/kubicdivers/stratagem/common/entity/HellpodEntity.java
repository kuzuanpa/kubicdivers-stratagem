package cn.kuzuanpa.kubicdivers.stratagem.common.entity;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractHellpodStratagem.DROP_LENGTH;

public class HellpodEntity extends Entity {
    public int age = 0;
    public long landTime = -1;
    int targetHeight = 0;
    public HellpodEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.setDeltaMovement(0, -3.0,0);
    }

    public HellpodEntity(Level level, Vec3 pos) {
        super(ModEntities.HELLPOD.get(), level);
        this.setPos(pos.x + 0.5, pos.y, pos.z + 0.5);
        this.setDeltaMovement(0, -3.0,0);
        targetHeight = (int) (getY() - DROP_LENGTH);
    }

    @Override
    public boolean save(@NotNull CompoundTag p_20224_) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
    }

    public void setTargetHeight(int targetHeight) {
        this.targetHeight = targetHeight;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }
    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        age = 1201; //set dead
    }
    @Override
    public void tick() {
        super.tick();
        age++;

        Vec3 movement = this.getDeltaMovement();
        this.setPos(this.getX() + movement.x, Math.max(targetHeight,this.getY() + movement.y), this.getZ() + movement.z);

        if (this.level().isClientSide) {
            if (onGround() && landTime == -1) {
                landTime = System.currentTimeMillis();
                for (int i = 0; i < 20; i++) this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(3), this.getRandomY(), this.getRandomZ(3), 0, 0.05, 0);
            }

            if (!onGround()) for (int i = 0; i < 4; i++) this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(4), this.getRandomY() + 1, this.getRandomZ(4), 0, 0.05, 0);
            return;
        }
        if(this.getY() > targetHeight) {
            for (int i = -3; i < 0 && getBlockY() + i >= targetHeight; i++) {
                this.level().setBlock(new BlockPos(this.getBlockX(), this.getBlockY()+i, this.getBlockZ()), Blocks.AIR.defaultBlockState(), 2);
            }
            List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.5).expandTowards(0,-3,0));
            for (Entity entity : list) {
                entity.hurt(this.damageSources().fall(), 100.0f);
            }
            if(this.onGround())this.setOnGround(false);
        }else {
            this.setDeltaMovement(0,0,0);
            this.setOnGround(true);
        }
        if(this.age > 1200)discard();

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        this.age = nbt.getInt("LifeTicks");
        this.targetHeight = nbt.getInt("targetHeight");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("LifeTicks", this.age);
        nbt.putInt("targetHeight", this.targetHeight);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canBeCollidedWith() { return true; }
}