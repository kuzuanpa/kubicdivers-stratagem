package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.oribital;

import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class OrbitalStrikeStratagem extends AbstractStrikeStratagem {
    @Override
    public String getId() {
        return "orbital_strike";
    }

    @Override
    public String getName() {
        return "Orbital Strike";
    }

    @Override
    public int getCooldown() {
        return 200;
    }

    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.RIGHT, Direction.UP);
    }
    @Override
    protected void executeOrbitalStrike(Level level, Vec3 targetPos) {
        strikeRandomInRadius(level, targetPos, 1.0f, 4.0f, false, Level.ExplosionInteraction.TNT);
    }
}