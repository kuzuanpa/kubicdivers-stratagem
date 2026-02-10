package cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.hellpod;

import cn.kuzuanpa.kubicdivers.stratagem.common.stratagem.types.AbstractHellpodStratagem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class WeaponHellpodStratagem extends AbstractHellpodStratagem {
    private final ItemStack weaponItem;
    private final String weaponName;

    public WeaponHellpodStratagem(String id, String name, ItemStack weaponItem) {
        this.weaponItem = weaponItem;
        this.weaponName = name;
    }

    @Override
    public String getId() {
        return "weapon_" + weaponItem.getItem().toString().toLowerCase();
    }

    @Override
    public String getName() {
        return weaponName + " Supply";
    }

    @Override
    public int getCooldown() {
        return 80;
    }

    @Nullable
    @Override
    public Entity createEntityForPad(Level level, Vec3 padPos) {
        return createItemFrame(level, padPos, weaponItem);
    }
}