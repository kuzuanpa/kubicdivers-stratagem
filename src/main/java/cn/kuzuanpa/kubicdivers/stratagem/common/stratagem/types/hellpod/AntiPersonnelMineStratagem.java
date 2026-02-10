package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.hellpod;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.APMineEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractHellpodStratagem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class AntiPersonnelMineStratagem extends AbstractHellpodStratagem {
    @Override
    public String getId() {
        return "ap_mine";
    }

    @Override
    public String getName() {
        return "Anti-Personnel Mines";
    }

    @Override
    public List<IStratagem.Direction> getSequence() {
        return List.of(
                IStratagem.Direction.DOWN, IStratagem.Direction.LEFT, IStratagem.Direction.UP,
                IStratagem.Direction.RIGHT, IStratagem.Direction.DOWN
        );
    }

    @Override
    public int getCooldown() {
        return 60;
    }

    @Nullable
    @Override
    public Entity createEntityForPad(Level level, Vec3 padPos) {
        APMineEntity mine = new APMineEntity(level);
        mine.setPos(padPos.x + 0.5, padPos.y, padPos.z + 0.5);

        for (int i = 0; i < 3; i++) {
            APMineEntity extraMine = new APMineEntity(level);
            extraMine.setPos(
                    padPos.x + level.random.nextIntBetweenInclusive(-2, 2) + 0.5,
                    padPos.y,
                    padPos.z + level.random.nextIntBetweenInclusive(-2, 2) + 0.5
            );
            level.addFreshEntity(extraMine);
        }

        return mine;
    }
}