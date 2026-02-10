package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.hellpod;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.MachineGunTurretEntity;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.IStratagem;
import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractHellpodStratagem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class MachineGunTurretStratagem extends AbstractHellpodStratagem {
    @Override
    public String getId() {
        return "machine_gun_turret";
    }

    @Override
    public String getName() {
        return "Machine Gun Turret";
    }

    @Override
    public List<IStratagem.Direction> getSequence() {
        return List.of(
                IStratagem.Direction.DOWN, IStratagem.Direction.UP, IStratagem.Direction.RIGHT,
                IStratagem.Direction.LEFT, IStratagem.Direction.RIGHT
        );
    }

    @Override
    public int getCooldown() {
        return 120;
    }

    @Nullable
    @Override
    public Entity createEntityForPad(Level level, Vec3 padPos) {
        MachineGunTurretEntity turret = new MachineGunTurretEntity(level);
        turret.setPos(padPos.x + 0.5, padPos.y, padPos.z + 0.5);
        return turret;
    }
}