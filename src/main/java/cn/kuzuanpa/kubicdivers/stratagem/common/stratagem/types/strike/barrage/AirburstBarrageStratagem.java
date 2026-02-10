package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.barrage;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AirburstBarrageStratagem extends AbstractStrikeStratagem {
    @Override public String getId() { return "airburst_barrage"; }

    @Override
    public String getName() {
        return "Airburst Barrage";
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "textures/gui/stratagem/airburst_barrage.png");
    }
    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.RIGHT, Direction.RIGHT);
    }
    @Override public int getDuration() { return 100; }

    @Override
    public void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {
        if (tick % 30 == 0 && level instanceof ServerLevel serverLevel) {
            Vec3 burstPos = targetPos.add(0,12,0);

            serverLevel.sendParticles(ParticleTypes.FLASH, burstPos.x, burstPos.y, burstPos.z, 5, 0.2, 0.2, 0.2, 0.0);

            for (int i = 0; i < 6; i++) {
                double r = level.random.nextDouble() * 6.0;
                double angle = level.random.nextDouble() * Math.PI * 2;
                Vec3 shrapnelPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING,
                        BlockPos.containing(targetPos.add((int)(Math.cos(angle) * r), 0, (int)(Math.sin(angle) * r)))).getCenter();

                level.explode(null, shrapnelPos.x, shrapnelPos.y,shrapnelPos.z,
                        2.5f, false, Level.ExplosionInteraction.NONE);
            }
        }
    }
}
