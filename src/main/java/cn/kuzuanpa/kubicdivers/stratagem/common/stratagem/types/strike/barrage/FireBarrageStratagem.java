package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.barrage;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FireBarrageStratagem extends AbstractStrikeStratagem {
    @Override
    public String getId() {
        return "fire_barrage";
    }

    @Override
    public String getName() {
        return "Fire Barrage";
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "textures/gui/stratagem/fire_barrage.png");
    }
    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP);
    }

    @Override public int getDuration() { return 300; }
    @Override
    public void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {
        if (tick % 15 == 0) {
            strikeRandomInRadius(level, beacon, targetPos, 15.0f, 3.5f, true, Level.ExplosionInteraction.TNT);
        }
    }
}
