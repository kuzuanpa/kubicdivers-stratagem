package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.strike.oribital;

import cn.kuzuanpa.kubicdivers.common.effect.ModEffects;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.OrbitalBullet;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractStrikeStratagem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod.MOD_ID;

public class GasStrikeStratagem extends AbstractStrikeStratagem {
    @Override
    public String getId() { return "gas_strike"; }
    @Override
    public String getName() { return "轨道毒气打击"; }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/stratagem/gas_strike.png");
    }
    @Override
    public List<Direction> getSequence() {
        return List.of(Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.RIGHT);
    }

    @Override
    public int getDuration() {
        return 160;
    }

    @Override
    public void activate(Level level, Vec3 targetPos, BeaconEntity beacon) {
        OrbitalBullet bullet = new OrbitalBullet(level, beacon.getOwner(),20.0F);
        bullet.setPos(targetPos.add(0,64,0));
        bullet.shoot(0, -1  , 0, 8.0F, 1.2F);
        level.addFreshEntity(bullet);
    }
    @Override
    public void tick(Level level, Vec3 targetPos, BeaconEntity beacon, int tick) {
        level.getEntitiesOfClass(LivingEntity.class,
                        new AABB(BlockPos.containing(targetPos)).inflate(8.0))
                .stream()
                .filter(e -> e.position().distanceTo(targetPos) <= 8.0f)
                .forEach(e -> {
                    e.addEffect(new MobEffectInstance(ModEffects.CONFUSION.get(), 100, 1));
                    e.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                });

        if(!(level instanceof ServerLevel))return;
        spawnRoundEffects(((ServerLevel) level), targetPos, 8, 24, ParticleTypes.GLOW);
    }
}