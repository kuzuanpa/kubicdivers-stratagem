package cn.kuzuanpa.kubicdivers.stratagem.common.entity;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class APMineEntity extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(APMineEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> ARMED =
            SynchedEntityData.defineId(APMineEntity.class, EntityDataSerializers.BOOLEAN);

    private int armTime = 40;
    private float damage = 8.0f;
    private float radius = 3.0f;

    public APMineEntity(EntityType<?> type, Level level) {
        super(type, level);
        setArmed(false);
    }

    public APMineEntity(Level level) {
        this(ModEntities.AP_MINE.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER, Optional.empty());
        this.entityData.define(ARMED, false);
    }

    public void setOwner(Player player) {
        this.entityData.set(OWNER, Optional.of(player.getUUID()));
    }

    public boolean isArmed() {
        return this.entityData.get(ARMED);
    }

    public void setArmed(boolean armed) {
        this.entityData.set(ARMED, armed);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (armTime > 0) {
                armTime--;
                if (armTime <= 0) {
                    setArmed(true);
                }
            }

            if (isArmed()) {
                checkTrigger();
            }
        }
    }

    private void checkTrigger() {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        if (nbt.hasUUID("Owner")) {
            this.entityData.set(OWNER, Optional.of(nbt.getUUID("Owner")));
        }
        setArmed(nbt.getBoolean("Armed"));
        armTime = nbt.getInt("ArmTime");
        damage = nbt.getFloat("Damage");
        radius = nbt.getFloat("Radius");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        this.entityData.get(OWNER).ifPresent(uuid -> nbt.putUUID("Owner", uuid));
        nbt.putBoolean("Armed", isArmed());
        nbt.putInt("ArmTime", armTime);
        nbt.putFloat("Damage", damage);
        nbt.putFloat("Radius", radius);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}