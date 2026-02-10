package cn.kuzuanpa.kubicdivers.stratagem.client.renderer;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import cn.kuzuanpa.kubicdivers.stratagem.common.entity.BeaconEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class BeaconRenderer extends EntityRenderer<BeaconEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KubicdiversStratagemMod.MOD_ID, "textures/entity/beacon_beam.png");

    public BeaconRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull BeaconEntity entity, float entityYaw, float partialTicks,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        int color = entity.getBeaconColor();
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = 0.75f;

        float height = entity.getBeaconHeight();

        float time = (entity.tickCount + partialTicks) * 0.05f;
        float pulse = (Mth.sin(time * 2.0f) * 0.2f + 0.8f);

        renderBeam(poseStack, buffer, r, g, b, a, height, pulse, partialTicks);
    }

    private void renderBeam(PoseStack poseStack, MultiBufferSource buffer,
                            float r, float g, float b, float a,
                            float height, float pulse, float partialTicks) {

        poseStack.pushPose();

        poseStack.translate(0.0, 0.5, 0.0);

        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

        float width = 0.5f * pulse;

        for (int i = 0; i < 4; i++) {
            float angle = (float) (i * Math.PI / 2.0);
            float x1 = Mth.sin(angle) * width;
            float z1 = Mth.cos(angle) * width;
            float x2 = Mth.sin(angle + (float) Math.PI) * width;
            float z2 = Mth.cos(angle + (float) Math.PI) * width;

            vertexConsumer.vertex(matrix, x1, height, z1)
                    .color(r, g, b, a)
                    .uv(0, 1)
                    .overlayCoords(0, 10)
                    .uv2(240)
                    .normal(0, 1, 0)
                    .endVertex();

            vertexConsumer.vertex(matrix, x2, height, z2)
                    .color(r, g, b, a)
                    .uv(1, 1)
                    .overlayCoords(0, 10)
                    .uv2(240)
                    .normal(0, 1, 0)
                    .endVertex();

            vertexConsumer.vertex(matrix, x2, 0, z2)
                    .color(r, g, b, a * 0.5f)
                    .uv(1, 0)
                    .overlayCoords(0, 10)
                    .uv2(240)
                    .normal(0, -1, 0)
                    .endVertex();

            vertexConsumer.vertex(matrix, x1, 0, z1)
                    .color(r, g, b, a * 0.5f)
                    .uv(0, 0)
                    .overlayCoords(0, 10)
                    .uv2(240)
                    .normal(0, -1, 0)
                    .endVertex();
        }

        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BeaconEntity entity) {
        return TEXTURE;
    }
}