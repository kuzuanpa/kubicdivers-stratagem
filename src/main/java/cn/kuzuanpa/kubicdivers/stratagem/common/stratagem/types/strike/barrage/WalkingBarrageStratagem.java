package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.barrage;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod.MOD_ID;

public class WalkingBarrageStratagem extends AbstractStrikeStratagem {
    @Override public int getDuration() { return 200; }

    @Override
    public String getId() {
        return "walking_barrage";
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/stratagem/walking_barrage.png");
    }
    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.DOWN);
    }
    @Override
    public String getName() {
        return "Walking Barrage";
    }

    @Override
    public void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {
        if (tick % 15 == 0) {
            int wave = tick / 20;
            float stepDistance = 6.0f;
            float totalOffset = wave * stepDistance;

            float yawRad = beacon.getYRot() * (float) (Math.PI / 180.0);
            double dx = -Mth.sin(yawRad) * totalOffset;
            double dz = Mth.cos(yawRad) * totalOffset;

            Vec3 currentCenter = new Vec3((int)(targetPos.x + dx), targetPos.y, (int)(targetPos.z + dz));

            float sideRad = (beacon.getYRot() + 90.0f) * (float) (Math.PI / 180.0);
            double sdx = -Mth.sin(sideRad);
            double sdz = Mth.cos(sideRad);

            for (int i = -1; i <= 1; i++) {
                Vec3 scatterPos = new Vec3((int)(currentCenter.x + i * sdx * 4.0), currentCenter.y, (int)(currentCenter.z + i * sdz * 4.0));

                strikeRandomInRadius(level, beacon, scatterPos, 2.5f, 4.5f, false, Level.ExplosionInteraction.TNT);
            }

        }
    }

}
