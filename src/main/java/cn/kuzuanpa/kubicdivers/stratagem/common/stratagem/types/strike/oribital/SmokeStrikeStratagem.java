package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.oribital;

import cn.kuzuanpa.kubicdivers.common.effect.ModEffects;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SmokeStrikeStratagem extends AbstractStrikeStratagem {
    @Override
    public String getId() { return "smoke_strike"; }
    @Override
    public String getName() { return "轨道烟雾攻击"; }

    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.UP);
    }


    @Override
    public int getDuration() {
        return 160;
    }
    @Override
    public void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {
        level.getEntitiesOfClass(LivingEntity.class,
                        new AABB(BlockPos.containing(targetPos)).inflate(8.0))
                .stream()
                .filter(e -> e.position().distanceTo(targetPos) <= 8.0f)
                .forEach(e -> {
                    e.addEffect(new MobEffectInstance(ModEffects.SMOKE.get(), 100, 1));
                });

        if(!(level instanceof ServerLevel))return;
        if(tick % 4 == 0) spawnRoundEffects(((ServerLevel) level), targetPos, 8, 24, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
    }
}
