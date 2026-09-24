package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.hellpod;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.AbstractSentryEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.GatlingSentryEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractHellpodStratagem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class GatlingSentryStratagem extends AbstractHellpodStratagem {
    @Override
    public String getId() {
        return "gatling_sentry";
    }

    @Override
    public String getName() {
        return "Gatling Sentry";
    }

    @Override
    public List<IStratagem.Direction> getSequence() {
        return List.of(
                IStratagem.Direction.DOWN, IStratagem.Direction.UP, IStratagem.Direction.RIGHT,
                IStratagem.Direction.LEFT
        );
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "textures/gui/stratagem/gatling_sentry.png");
    }
    @Override
    public int getCooldown() {
        return 120;
    }

    @Nullable
    @Override
    public Entity createEntityOnPod(Level level, Vec3 padPos) {
        AbstractSentryEntity sentry = new GatlingSentryEntity(level);
        sentry.setPos(padPos.x + 0.5, padPos.y, padPos.z + 0.5);
        return sentry;
    }
}