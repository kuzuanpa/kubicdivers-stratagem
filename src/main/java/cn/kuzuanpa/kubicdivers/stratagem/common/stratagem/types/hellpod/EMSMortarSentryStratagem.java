package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.hellpod;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.AbstractSentryEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.EMSMortarSentryEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractHellpodStratagem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class EMSMortarSentryStratagem extends AbstractHellpodStratagem {
    @Override
    public String getId() {
        return "mortar_sentry_ems";
    }

    @Override
    public String getName() {
        return "EMS Mortar Sentry";
    }

    @Override
    public List<Direction> getSequence() {
        return List.of(
                Direction.DOWN, Direction.UP, Direction.RIGHT,
                Direction.DOWN, Direction.RIGHT
        );
    }

    @Override
    public @Nullable ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "textures/gui/stratagem/ems_mortar_sentry.png");
    }
    @Override
    public int getCooldown() {
        return 120;
    }

    @Nullable
    @Override
    public Entity createEntityOnPod(Level level, Vec3 padPos) {
        AbstractSentryEntity sentry = new EMSMortarSentryEntity(level);
        sentry.setPos(padPos.x + 0.5, padPos.y, padPos.z + 0.5);
        return sentry;
    }
}