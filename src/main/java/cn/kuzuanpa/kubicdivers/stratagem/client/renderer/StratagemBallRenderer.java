package cn.kuzuanpa.kubicdivers.stratagem.client.renderer;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.StratagemBallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StratagemBallRenderer extends EntityRenderer<StratagemBallEntity> {
    private final ItemRenderer itemRenderer;

    public StratagemBallRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(StratagemBallEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        ItemStack itemstack = entity.getItem();
        if (itemstack.isEmpty()) return;

        float rotation = (entity.tickCount + partialTicks) * 50.0F % 360.0F;
        float bounce = Mth.sin((entity.tickCount + partialTicks) * 0.1F) * 0.05F;

        poseStack.translate(0.0D, bounce, 0.0D);


        BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, entity.level(), null, entity.getId());

        this.itemRenderer.render(itemstack, ItemDisplayContext.GUI, false, poseStack, buffer,
                packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull StratagemBallEntity entity) {
        return null;
    }
}