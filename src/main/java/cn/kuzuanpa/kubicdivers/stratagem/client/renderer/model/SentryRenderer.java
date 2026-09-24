package cn.kuzuanpa.kubicdivers.stratagem.client.renderer.model;

import cn.kuzuanpa.kubicdivers.stratagem.client.ClientLayerRegistry;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.AbstractSentryEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod.MOD_ID;

public class SentryRenderer<T extends AbstractSentryEntity> extends MobRenderer<T, SentryModel<T>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/sentry/gatling_sentry.png");

    public SentryRenderer(EntityRendererProvider.Context context) {
        // 这里需要在ClientSetup中注册LayerLocation
        super(context, new SentryModel<>(context.bakeLayer(ClientLayerRegistry.SENTRY_LAYER)), 10F);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}