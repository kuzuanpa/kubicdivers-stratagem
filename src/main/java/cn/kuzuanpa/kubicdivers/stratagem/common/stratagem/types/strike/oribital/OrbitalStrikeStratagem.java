package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.oribital;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod.MOD_ID;

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
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/stratagem/orbital_strike.png");
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
    public void activate(Level level, Vec3 targetPos, BeaconEntity beacon) {
        strikeRandomInRadius(level, beacon, targetPos, 1.0f, 4.0f, false, Level.ExplosionInteraction.TNT);
    }
}