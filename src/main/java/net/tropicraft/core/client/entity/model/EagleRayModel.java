package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.underdasea.EagleRayEntity;

public class EagleRayModel extends HierarchicalModel<EagleRayEntity> {
    /**
     * Wing joint amplitudes, linearly interpolated between previous tick and this tick using partialTicks.
     */
    private final float[] interpolatedWingAmplitudes = new float[EagleRayEntity.WING_JOINTS];

    private final ModelPart root;

    public EagleRayModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-2.0f, 0.0f, 0.0f, 5, 3, 32), PartPose.offset(0.0f, 0.0f, -8.0f));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(EagleRayEntity eagleRay, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
        renderWings(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
        renderTailSimple(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
    }

    private void renderTailSimple(PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, int color) {
        float minU = 0.75f;
        float maxU = 1.0f;
        float minV = 0.0f;
        float maxV = 0.5f;

        stack.pushPose();
        stack.translate(0.55f, 0.0f, 1.5f);
        stack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        stack.scale(1.5f, 1.0f, 1.0f);
        vertex(buffer, stack.last(), 0, 0, 0, color, minU, minV, packedLightIn, packedOverlayIn);
        vertex(buffer, stack.last(), 0, 0, 1, color, minU, maxV, packedLightIn, packedOverlayIn);
        vertex(buffer, stack.last(), 1, 0, 1, color, maxU, maxV, packedLightIn, packedOverlayIn);
        vertex(buffer, stack.last(), 1, 0, 0, color, maxU, minV, packedLightIn, packedOverlayIn);
        stack.popPose();
    }

    private static void vertex(VertexConsumer bufferIn, PoseStack.Pose pose, float x, float y, float z, int color, float texU, float texV, int packedLight, int packedOverlay) {
        bufferIn.addVertex(pose, x, y, z).setColor(color).setUv(texU, texV).setOverlay(packedOverlay).setLight(packedLight).setNormal(pose, 0.0f, -1.0f, 0.0f);
    }

    private void renderWings(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5f / 16.0f, 0, -0.5f); // Center on body
        matrixStackIn.scale(2.0f, 0.5f, 2.0f); // Scale to correct size

        renderWing(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color, false);

        // Rotate around center
        matrixStackIn.translate(0, 0, 0.5f);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0f));
        matrixStackIn.translate(0, 0, -0.5f);

        renderWing(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color, true);

        matrixStackIn.popPose();
    }

    private void renderWing(PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, int color, boolean reverse) {
        float minUFront = 0.0f;
        float maxUFront = 0.25f;
        float minVFront = 0.0f;
        float maxVFront = 0.5f;

        float minUBack = 0.0f;
        float maxUBack = 0.25f;
        float minVBack = 0.5f;
        float maxVBack = 1.0f;

        stack.pushPose();
        stack.translate(1.25f / 16.0f, 0, 0); // Translate out to body edge

        for (int i = 1; i < EagleRayEntity.WING_JOINTS; i++) {
            float prevAmplitude = interpolatedWingAmplitudes[i - 1];
            float amplitude = interpolatedWingAmplitudes[i];

            float prevX = (i - 1) / (EagleRayEntity.WING_JOINTS - 1.0f);
            float x = i / (EagleRayEntity.WING_JOINTS - 1.0f);

            float prevUFront = minUFront + (maxUFront - minUFront) * prevX;
            float uFront = minUFront + (maxUFront - minUFront) * x;
            float prevUBack = minUBack + (maxUBack - minUBack) * prevX;
            float uBack = minUBack + (maxUBack - minUBack) * x;

            float offset = -0.001f;
            // Bottom
            PoseStack.Pose pose = stack.last();

            vertex(buffer, pose, x, amplitude - offset, 0, color, uBack, reverse ? maxVBack : minVBack, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, x, amplitude - offset, 1, color, uBack, reverse ? minVBack : maxVBack, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, prevX, prevAmplitude - offset, 1, color, prevUBack, reverse ? minVBack : maxVBack, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, prevX, prevAmplitude - offset, 0, color, prevUBack, reverse ? maxVBack : minVBack, packedLightIn, packedOverlayIn);

            // Top
            vertex(buffer, pose, prevX, prevAmplitude, 0, color, prevUFront, reverse ? maxVFront : minVFront, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, prevX, prevAmplitude, 1, color, prevUFront, reverse ? minVFront : maxVFront, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, x, amplitude, 1, color, uFront, reverse ? minVFront : maxVFront, packedLightIn, packedOverlayIn);
            vertex(buffer, pose, x, amplitude, 0, color, uFront, reverse ? maxVFront : minVFront, packedLightIn, packedOverlayIn);
        }

        stack.popPose();
    }

    @Override
    public void prepareMobModel(EagleRayEntity ray, float par2, float par3, float partialTicks) {
        float[] prevWingAmplitudes = ray.getPrevWingAmplitudes();
        float[] wingAmplitudes = ray.getWingAmplitudes();

        for (int i = 1; i < EagleRayEntity.WING_JOINTS; i++) {
            float prevWingAmplitude = prevWingAmplitudes[i];
            float wingAmplitude = wingAmplitudes[i];
            interpolatedWingAmplitudes[i] = prevWingAmplitude + partialTicks * (wingAmplitude - prevWingAmplitude);
        }
    }
}
