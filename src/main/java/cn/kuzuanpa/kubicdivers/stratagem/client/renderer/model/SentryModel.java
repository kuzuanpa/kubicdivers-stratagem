package cn.kuzuanpa.kubicdivers.stratagem.client.renderer.model;

import cn.kuzuanpa.kubicdivers.stratagem.common.entity.sentry.AbstractSentryEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class SentryModel<T extends Entity> extends EntityModel<T> {
    private final ModelPart head;
    private final ModelPart barrels;
    private final ModelPart bb_main;

    public SentryModel(ModelPart root) {
        this.head = root.getChild("head");
        this.barrels = this.head.getChild("barrels");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition barrels = head.addOrReplaceChild("barrels", CubeListBuilder.create().texOffs(-17, -17).addBox(-1.0F, -1.0F, -18.0F, 2.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(-18, -14).addBox(-8.0F, -9.0F, -8.0F, 16.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(-18, -14).addBox(-8.0F, -19.0F, -8.0F, 1.0F, 10.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(-9, -14).addBox(7.0F, -19.0F, -8.0F, 1.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.barrels  .zRot = ((AbstractSentryEntity) entity).isFiring()? (System.currentTimeMillis()%3600) / 10F : 0;

        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.bb_main.yRot = netHeadYaw * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}