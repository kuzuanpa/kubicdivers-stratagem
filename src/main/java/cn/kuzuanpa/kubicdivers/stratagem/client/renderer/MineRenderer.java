package cn.kuzuanpa.kubicdivers.stratagem.client.renderer;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.APMineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MineRenderer extends EntityRenderer<APMineEntity> {

    public MineRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull APMineEntity entity, float entityYaw, float partialTicks,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull APMineEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}