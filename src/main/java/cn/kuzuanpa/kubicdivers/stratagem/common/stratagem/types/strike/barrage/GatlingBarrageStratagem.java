package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.barrage;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GatlingBarrageStratagem extends AbstractStrikeStratagem {
    @Override public String getId() { return "gatling_barrage"; }

    @Override
    public String getName() {
        return "Gatling Barrage";
    }

    @Override public int getDuration() { return 200; }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "textures/gui/stratagem/gatling_barrage.png");
    }
    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP, Direction.UP);
    }
    @Override
    public void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {
        if (tick % 2 == 0) {
            strikeRandomInRadius(level, targetPos, 5.0f, 1.0f, false, Level.ExplosionInteraction.NONE);
        }
    }
}
