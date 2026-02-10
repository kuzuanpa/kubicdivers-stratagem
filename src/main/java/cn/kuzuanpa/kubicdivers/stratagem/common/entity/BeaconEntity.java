package cn.kuzuanpa.kubicdivers.stratagem.common.entity;

import cn.kuzuanpa.kubicdivers.IKubicDiverHUD;
import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import cn.kuzuanpa.kubicdivers.stratagem.common.PlayerCooldownManager;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.StratagemManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BeaconEntity extends Entity implements IKubicDiverHUD {
    static final EntityDataAccessor<String> STRATAGEM_ID = SynchedEntityData.defineId(BeaconEntity.class, EntityDataSerializers.STRING);
    static final EntityDataAccessor<Integer> BEACON_COLOR = SynchedEntityData.defineId(BeaconEntity.class, EntityDataSerializers.INT);
    static final EntityDataAccessor<Float> BEACON_HEIGHT = SynchedEntityData.defineId(BeaconEntity.class, EntityDataSerializers.FLOAT);

    @Nullable private UUID ownerUUID;
    @Nullable private Entity cachedOwner;
    int age = 0;
    public int activationDelay = 100;
    boolean preActivated = false;
    @NotNull public Vec3 targetPos;
    @Nullable public Entity targetEntity;
    @Nullable public IStratagem runningStratagem;
    @Nullable public HellpodEntity hellpod;

    public BeaconEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.targetPos = new Vec3((int) xo,(int) yo,(int) zo);
    }

    public BeaconEntity(Level level, Vec3 pos, String stratagemId) {
        super(ModEntities.BEACON.get(), level);
        this.setPos(pos.x + 0.5, pos.y, pos.z + 0.5);
        this.targetPos = pos;
        setStratagemId(stratagemId);

        setBeaconHeight(10.0f);

        IStratagem stratagem = StratagemManager.getStratagem(stratagemId);
        if (stratagem == null) return;

        activationDelay = stratagem.getActivationDelay();
        if (stratagem.getType().equals(IStratagem.StratagemType.STRIKE)) setBeaconColor(0xFF0000);
        else setBeaconColor(0x0000FF);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(STRATAGEM_ID, "");
        this.entityData.define(BEACON_COLOR, 0xFF0000);
        this.entityData.define(BEACON_HEIGHT, 10.0f);
    }

    public void setStratagemId(String id) {this.entityData.set(STRATAGEM_ID, id);}
    public String getStratagemId() {return this.entityData.get(STRATAGEM_ID);}
    public void setBeaconColor(int color) {this.entityData.set(BEACON_COLOR, color);}
    public int getBeaconColor() {return this.entityData.get(BEACON_COLOR);}
    public void setBeaconHeight(float height) {this.entityData.set(BEACON_HEIGHT, height);}
    public float getBeaconHeight() {return this.entityData.get(BEACON_HEIGHT);}

    public void setOwner(@Nullable Entity p_37263_) {
        if (p_37263_ != null) {
            this.ownerUUID = p_37263_.getUUID();
            this.cachedOwner = p_37263_;
            setYRot(cachedOwner.getYRot());
        }
    }

    public Player getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return (Player) this.cachedOwner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            return (Player) this.cachedOwner;
        } else {
            return null;
        }
    }
    @Override
    public void tick() {
        super.tick();
        age++;
        float pulse = (float) Math.sin(age * 0.2) * 0.5f + 0.5f;
        setBeaconHeight(32.0f + pulse * 16.0f);

        if (this.level().isClientSide) return;

        IStratagem stratagem = StratagemManager.getStratagem(getStratagemId());
        if (stratagem == null) return;

        int remainingTime = activationDelay - age;
        if (remainingTime <= stratagem.getPreActivateTime()&&!preActivated) {
            stratagem.preActivate(this.level(), targetPos, this);
            preActivated = true;
        }

        if (age < activationDelay) return;

        if (runningStratagem == null) {
            startStratagem();
            return;
        }

        runningStratagem.tick(this.level(), targetPos, this, age - activationDelay);

        if(age > activationDelay + runningStratagem.getDuration())this.discard();
    }

    private void startStratagem() {
        runningStratagem = StratagemManager.getStratagem(getStratagemId());
        if (runningStratagem != null) {
            runningStratagem.activate(this.level(), targetPos, this);

            PlayerCooldownManager.startCooldown(getOwner(), runningStratagem);

            PlayerCooldownManager.syncCooldownToClient(getOwner());
        }else this.discard();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        setStratagemId(nbt.getString("StratagemId"));
        setBeaconColor(nbt.getInt("BeaconColor"));
        age = nbt.getInt("Age");
        activationDelay = nbt.getInt("ActivationDelay");
        if (nbt.contains("TargetPos")) targetPos = new Vec3(nbt.getDouble("TargetPosX"), nbt.getDouble("TargetPosY"), nbt.getDouble("TargetPosZ"));
        if (nbt.hasUUID("Owner")) {
            this.ownerUUID = nbt.getUUID("Owner");
            this.cachedOwner = null;
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        nbt.putString("StratagemId", getStratagemId());
        nbt.putInt("BeaconColor", getBeaconColor());
        nbt.putInt("Age", age);
        nbt.putInt("ActivationDelay", activationDelay);
        nbt.putDouble("TargetPosX", targetPos.x);
        nbt.putDouble("TargetPosY", targetPos.y);
        nbt.putDouble("TargetPosZ", targetPos.z);
        if (this.ownerUUID != null) nbt.putUUID("Owner", this.ownerUUID);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public List<Component> getHUDText() {
        IStratagem stratagem = StratagemManager.getStratagem(getStratagemId());
        if(stratagem == null)return new ArrayList<>();
        int timeRemaining = age - stratagem.getActivationDelay();
        String state = timeRemaining < 0 || stratagem.getDuration() <= 0? "即将到达: "+ (-timeRemaining/20) : "正在进行: " + ((stratagem.getDuration() - timeRemaining)/20);
        return Arrays.asList(Component.literal(stratagem.getName()),Component.literal(state));
    }

    @Override
    public @Nullable ResourceLocation getHUDIcon() {
        IStratagem stratagem = StratagemManager.getStratagem(getStratagemId());
        if(stratagem == null)return null;
        return stratagem.getIcon();
    }
}