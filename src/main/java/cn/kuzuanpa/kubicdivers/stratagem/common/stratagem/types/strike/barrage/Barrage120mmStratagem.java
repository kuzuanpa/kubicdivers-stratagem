package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.barrage;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// 120mm
public class Barrage120mmStratagem extends AbstractStrikeStratagem {
    @Override
    public String getId() {
        return "120mm_barrage";
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "textures/gui/stratagem/120mm_barrage.png");
    }

    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.DOWN);
    }
    @Override
    public String getName() {
        return "120mm Barrage";
    }

    @Override public int getDuration() { return 300; }
    @Override
    public void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {
        if (tick % 12 == 0) strikeRandomInRadius(level, targetPos, 12.0f, 4.0f, false, Level.ExplosionInteraction.TNT);
    }
}
