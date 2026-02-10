package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.oribital;

import cn.kuzuanpa.kubicdivers.common.event.KubicEntityHelper;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class AutoAimStrikeStratagem extends AbstractStrikeStratagem {
    @Override
    public String getId() { return "railcannon_strike"; }
    @Override
    public String getName() { return "轨道自动瞄准打击"; }

    @Override
    public int getPreActivateTime() {
        return 10;
    }

    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.UP, Direction.DOWN, Direction.DOWN, Direction.RIGHT);
    }
    @Override
    public void preActivate(Level level, Vec3 targetPos, BeaconEntity beacon) {
        beacon.targetEntity = level.getEntitiesOfClass(LivingEntity.class,
                        new AABB(BlockPos.containing(targetPos)).inflate(10.0))
                .stream()
                .filter(e -> KubicEntityHelper.getKubicEntity(e) != null)
                .max(Comparator.comparingInt(e -> KubicEntityHelper.getKubicEntity(e).getLevel()))
                .orElse(null);
    }

    @Override
    public int getDuration() {
        return 40;
    }

    @Override
    public void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {
        if(!(level instanceof ServerLevel))return;
        spawnOrbitalVisual((ServerLevel) level, targetPos, ParticleTypes.SMALL_FLAME, ParticleTypes.SMOKE);
        if(tick >= getDuration()) strike(level, targetPos.x, targetPos.y,targetPos.z, 4.0f, false, Level.ExplosionInteraction.TNT, true);
        if(beacon.targetEntity == null)return;
        beacon.targetPos = beacon.targetPos.add(beacon.targetEntity.position().add(beacon.targetPos.reverse()).scale(.2));
    }

}