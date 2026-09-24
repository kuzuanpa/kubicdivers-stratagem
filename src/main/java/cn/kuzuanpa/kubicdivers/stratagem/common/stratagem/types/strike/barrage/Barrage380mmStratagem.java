package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.barrage;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod.MOD_ID;

// 120mm
public class Barrage380mmStratagem extends AbstractStrikeStratagem {
    @Override
    public String getId() {
        return "380mm_barrage";
    }

    @Override
    public String getName() {
        return "380mm Barrage";
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/stratagem/380mm_barrage.png");
    }
    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.DOWN, Direction.UP, Direction.UP, Direction.LEFT, Direction.DOWN, Direction.DOWN);
    }
    @Override public int getDuration() { return 300; } // 15秒
    @Override
    public void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {
        if (tick % 12 == 0) strikeRandomInRadius(level, beacon, targetPos, 32.0f, 8.0f, false, Level.ExplosionInteraction.TNT);
    }
}
