package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.hellpod;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.AbstractSentryEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.CannonSentryEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractHellpodStratagem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class CannonSentryStratagem extends AbstractHellpodStratagem {
    @Override
    public String getId() {
        return "cannon_sentry";
    }

    @Override
    public String getName() {
        return "Cannon Sentry";
    }

    @Override
    public List<Direction> getSequence() {
        return List.of(
                Direction.DOWN, Direction.UP, Direction.RIGHT,
                Direction.UP, Direction.LEFT, Direction.UP
        );
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "textures/gui/stratagem/cannon_sentry.png");
    }
    @Override
    public int getCooldown() {
        return 120;
    }

    @Nullable
    @Override
    public Entity createEntityOnPod(Level level, Vec3 padPos) {
        AbstractSentryEntity sentry = new CannonSentryEntity(level);
        sentry.setPos(padPos.x + 0.5, padPos.y, padPos.z + 0.5);
        return sentry;
    }
}