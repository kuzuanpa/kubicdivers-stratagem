package cn.kuzuanpa.kubicdivers.stratagem.client.renderer;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.projectile.SentryBullet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod.MOD_ID;


public class SentryBulletRenderer extends EntityRenderer<AbstractArrow> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath( MOD_ID, "textures/entity/sentry_bullet.png");

    public SentryBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(AbstractArrow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch));

        poseStack.scale(0.4F, 0.1F, 0.1F);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));

        PoseStack.Pose lastPose = poseStack.last();
        Matrix4f poseMatrix = lastPose.pose();
        Matrix3f normalMatrix = lastPose.normal();

        drawPlane(poseMatrix, normalMatrix, vertexConsumer, 15728880);

        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        drawPlane(poseMatrix, normalMatrix, vertexConsumer, 15728880);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void drawPlane(Matrix4f pose, Matrix3f normal, VertexConsumer consumer, int light) {
        addVertex(pose, normal, consumer, -0.5f, 0, -0.5f, 0, 0, light);
        addVertex(pose, normal, consumer, 0.5f, 0, -0.5f, 1, 0, light);
        addVertex(pose, normal, consumer, 0.5f, 0, 0.5f, 1, 1, light);
        addVertex(pose, normal, consumer, -0.5f, 0, 0.5f, 0, 1, light);

        addVertex(pose, normal, consumer, -0.5f, 0, 0.5f, 0, 1, light);
        addVertex(pose, normal, consumer, 0.5f, 0, 0.5f, 1, 1, light);
        addVertex(pose, normal, consumer, 0.5f, 0, -0.5f, 1, 0, light);
        addVertex(pose, normal, consumer, -0.5f, 0, -0.5f, 0, 0, light);
    }

    private void addVertex(Matrix4f pose, Matrix3f normal, VertexConsumer consumer, float x, float y, float z, float u, float v, int light) {
        consumer.vertex(pose, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0f, 1f, 0f)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractArrow entity) {
        return TEXTURE;
    }
}