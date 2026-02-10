package cn.kuzuanpa.kubicdivers.stratagem.client.renderer;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.HellpodEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class HellpodRenderer extends EntityRenderer<HellpodEntity> {

    static final ResourceLocation BASE_MODEL_LOC = ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "hellpod-main");
    public HellpodRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(HellpodEntity entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));

        BakedModel baseModel = Minecraft.getInstance().getModelManager().getModel(BASE_MODEL_LOC);

        poseStack.pushPose();
        renderBakedModel(baseModel, poseStack, buffer, light);
        poseStack.popPose();

        float progress = entity.landTime > 0? (System.currentTimeMillis() - 1000 - entity.landTime)/200F : 0;
        if(progress > 1.2f){
            super.render(entity, yaw, partialTick, poseStack, buffer, light);
            return;
        }

        if (progress > 0 && progress < 0.6f) {
            float hatchProg = Mth.clamp(progress / 0.6f, 0, 1);
            poseStack.pushPose();
            poseStack.translate(0, Math.sin(hatchProg * Math.PI / 2) * 8.0, hatchProg * 4.0);
            poseStack.mulPose(Axis.XP.rotationDegrees(hatchProg * 120));
            //renderBakedModel(hatchModel, poseStack, buffer, light);
            poseStack.popPose();
        }
        //else renderBakedModel(hatchModel, poseStack, buffer, light);

        float riseProg = Mth.clamp((progress - 0.2f) / 0.6f, 0, 1);
        float smoothedRise = 1 - (1 - riseProg) * (1 - riseProg);

        poseStack.pushPose();
        poseStack.translate(0, smoothedRise * 1.5f, 0);
        //renderBakedModel(internalModel, poseStack, bufferSource, light);
        poseStack.popPose();

        super.render(entity, yaw, partialTick, poseStack, buffer, light);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull HellpodEntity p_114482_) {
        return InventoryMenu.BLOCK_ATLAS;
    }
    private void renderBakedModel(BakedModel model, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));

        for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(42), ModelData.builder().build(), null)) {
            consumer.putBulkData(poseStack.last(), quad, 1.0F, 1.0F, 1.0F, light, OverlayTexture.NO_OVERLAY);
        }
    }
}