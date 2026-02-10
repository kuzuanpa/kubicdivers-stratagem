package cn.kuzuanpa.kubicdivers.stratagem.common.entity;

import cn.kuzuanpa.kubicdivers.IKubicDiverHUD;
import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MachineGunTurretEntity extends Entity implements IKubicDiverHUD {
    private static final EntityDataAccessor<Optional<UUID>> OWNER =
            SynchedEntityData.defineId(MachineGunTurretEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> AMMO =
            SynchedEntityData.defineId(MachineGunTurretEntity.class, EntityDataSerializers.INT);

    private int lifespan = 12000;

    public MachineGunTurretEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public MachineGunTurretEntity(Level level) {
        this(ModEntities.MACHINE_GUN_TURRET.get(), level);
        setAmmo(200);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER, Optional.empty());
        this.entityData.define(AMMO, 0);
    }

    public void setOwner(Player player) {
        this.entityData.set(OWNER, Optional.of(player.getUUID()));
    }

    @Override
    public void tick() {
        super.tick();
    }
    public int getAmmo() {
        return this.entityData.get(AMMO);
    }

    public void setAmmo(int ammo) {
        this.entityData.set(AMMO, Math.max(0, ammo));
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        if (nbt.hasUUID("Owner")) {
            this.entityData.set(OWNER, Optional.of(nbt.getUUID("Owner")));
        }
        setAmmo(nbt.getInt("Ammo"));
        lifespan = nbt.getInt("Lifespan");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        this.entityData.get(OWNER).ifPresent(uuid -> nbt.putUUID("Owner", uuid));
        nbt.putInt("Ammo", getAmmo());
        nbt.putInt("Lifespan", lifespan);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public List<Component> getHUDText() {
        return List.of(Component.literal("TEST: DEMOCRACY..."));
    }
}