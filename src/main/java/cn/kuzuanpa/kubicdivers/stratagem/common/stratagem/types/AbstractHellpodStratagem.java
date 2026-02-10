package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.HellpodEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractHellpodStratagem implements IStratagem {
    protected static final List<Direction> DEFAULT_SEQUENCE = List.of(
            Direction.DOWN, Direction.DOWN, Direction.UP, Direction.RIGHT
    );
    public static final int DROP_LENGTH = 80;

    @Override
    public List<Direction> getSequence() {
        return DEFAULT_SEQUENCE;
    }

    @Override
    public StratagemType getType() {
        return StratagemType.HELLPOD;
    }

    @Override
    public int getCooldown() {
        return 100;
    }
    @Override
    public void preActivate(Level level, Vec3 targetPos, BeaconEntity beacon) {
        if (!level.isClientSide) {

            Vec3 pos = new Vec3(targetPos.x, targetPos.y+ DROP_LENGTH, targetPos.z);
            beacon.hellpod = new HellpodEntity(level, pos);
            level.addFreshEntity(beacon.hellpod);

            KubicdiversStratagemMod.LOGGER.info("Hellpod incoming at {}", targetPos);
        }
    }
    @Override
    public void activate(Level level, Vec3 targetPos, BeaconEntity beacon) {
        if (!level.isClientSide) {
            if(beacon.hellpod == null)return;
//createHellpodPad(level, targetPos);
            Entity entity = createEntityForPad(level, targetPos);
            if (entity != null) {
                level.addFreshEntity(entity);
                entity.startRiding(beacon.hellpod);
                KubicdiversStratagemMod.LOGGER.info("Hellpod deployed {} at {}",
                        entity.getDisplayName().getString(), targetPos);
            }
        }
    }

    @Nullable
    public abstract Entity createEntityForPad(Level level, Vec3 padPos);

    protected ItemFrame createItemFrame(Level level, Vec3 pos, ItemStack item) {
        ItemFrame itemFrame = new ItemFrame(EntityType.ITEM_FRAME, level);
        itemFrame.setPos(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
        itemFrame.setItem(item.copy());
        return itemFrame;
    }
}