package cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry;

import cn.kuzuanpa.kubicdivers.stratagem.common.ModEntities;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.MortarEMSProjectile;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.MortarShellProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

public class EMSMortarSentryEntity extends MortarSentryEntity{
    public EMSMortarSentryEntity(EntityType<EMSMortarSentryEntity> type, Level level) {
        super(type, level);
    }

    public EMSMortarSentryEntity(Level level) {
        super(ModEntities.EMS_MORTAR_SENTRY.get(), level);
    }
    protected AbstractArrow createBullet(LivingEntity target){
        return new MortarEMSProjectile(this.level(), this);
    }
    @Override protected int getFireRate() { return 120; }

}
