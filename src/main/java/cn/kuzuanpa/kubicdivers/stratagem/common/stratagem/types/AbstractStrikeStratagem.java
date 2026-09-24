package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.OrbitalBullet;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.SentryBullet;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class AbstractStrikeStratagem implements IStratagem {
    protected static final List<Direction> DEFAULT_SEQUENCE = List.of(
            Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.DOWN
    );

    @Override
    public List<Direction> getSequence() {
        return DEFAULT_SEQUENCE;
    }

    @Override
    public StratagemType getType() {
        return StratagemType.STRIKE;
    }

    @Override
    public int getCooldown() {
        return 150;
    }

    @Override
    public void activate(Level level, Vec3 targetPos, BeaconEntity beacon) {
        if (!level.isClientSide) {
            try {
                executeOrbitalStrike(level, targetPos);
                KubicdiversStratagemMod.LOGGER.info("Orbital strike executed at {}", targetPos);
            } catch (Exception e) {
                KubicdiversStratagemMod.LOGGER.error("Failed to execute orbital strike", e);
            }
        }
    }

    @Deprecated
    protected void executeOrbitalStrike(Level level, Vec3 targetPos){};

    protected void spawnOrbitalVisual(ServerLevel level, Vec3 targetPos, ParticleOptions coreParticle, ParticleOptions trailParticle) {
        double x = targetPos.x + 0.5;
        double y = targetPos.y + 0.5;
        double z = targetPos.z + 0.5;

        int beamHeight = 100;
        for (int i = 0; i < beamHeight; i+=2) {
            double particleY = y + i;
            level.sendParticles(coreParticle, x, particleY, z, 2, 0.05, 0.05, 0.05, 0.01);
            level.sendParticles(trailParticle, x, particleY, z, 2, 0.5, 0.5, 0.5, 0.01);
        }
        //level.sendParticles(ParticleTypes.FLASH, x, y + 0.5, z, 3, 0.2, 0.2, 0.2, 0.0);
    }

    protected void spawnRoundEffects(ServerLevel level, Vec3 centerPos, int range, int density, ParticleOptions coreParticle) {
        for (int i = 0; i < density; i++) {
            double x = centerPos.x + level.random.nextDouble() * range * 2 - range;
            double y = centerPos.y + level.random.nextDouble() * range * 2 - range;
            double z = centerPos.z + level.random.nextDouble() * range * 2 - range;
            level.sendParticles(coreParticle, x, y, z, 2, 0.05, 0.05, 0.05, 0.01);
        }
    }
    protected void strikeRandomInRadius(Level level, BeaconEntity beacon, Vec3 center, float scatterRadius, float explosionPower, boolean spawnFire, Level.ExplosionInteraction explodeType) {
        strikeRandomInRadius(level, beacon, center, scatterRadius, explosionPower, spawnFire, explodeType, true);
    }

    protected void strikeRandomInRadius(Level level, BeaconEntity beacon, Vec3 center, float scatterRadius, float explosionPower, boolean spawnFire, Level.ExplosionInteraction explodeType, boolean addOrbitalEffect) {
        double offsetX = (level.random.nextDouble() - 0.5) * 2 * scatterRadius;
        double offsetZ = (level.random.nextDouble() - 0.5) * 2 * scatterRadius;
        surfaceStrike(level, beacon, center.x + offsetX, center.z + offsetZ, explosionPower,spawnFire,explodeType,addOrbitalEffect);
    }

    protected void surfaceStrike(Level level, BeaconEntity beacon, double x, double z, float explosionPower, boolean spawnFire, Level.ExplosionInteraction explodeType, boolean addOrbitalEffect){
        int strikeHeight = level.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) x, (int) z);
        strike(level, beacon, x,strikeHeight,z,explosionPower,spawnFire,explodeType,addOrbitalEffect);
    }

    protected void strike(Level level, BeaconEntity beacon, double x, double y, double z, float explosionPower, boolean spawnFire, Level.ExplosionInteraction explodeType, boolean addOrbitalEffect){
        if (level instanceof ServerLevel serverLevel && addOrbitalEffect) {
            spawnOrbitalVisual(serverLevel,new Vec3(x,y, z), ParticleTypes.END_ROD, ParticleTypes.LARGE_SMOKE);
        }

        OrbitalBullet bullet = new OrbitalBullet(level, beacon.getOwner(),20.0F);
        bullet.setCallback(b->{
            level.explode(b, b.getX(), b.getY(), b.getZ(), explosionPower, spawnFire, Level.ExplosionInteraction.NONE);
            if(explodeType != Level.ExplosionInteraction.NONE)level.explode(b, b.getX(), b.getY(), b.getZ(), explosionPower/4F, spawnFire, explodeType);
            return true;
        });
        bullet.setPos(new Vec3(x,y + 64,z));
        bullet.shoot(0, -1  , 0, 8.0F, 1.2F);
        level.addFreshEntity(bullet);
    }
}