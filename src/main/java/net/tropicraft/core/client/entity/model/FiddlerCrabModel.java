package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class FiddlerCrabModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart claw_left_c_r1;
    private final ModelPart leg_left_fra;
    private final ModelPart leg_left_mia;
    private final ModelPart leg_left_baa;
    private final ModelPart leg_right_fra;
    private final ModelPart leg_right_mia;
    private final ModelPart leg_right_baa;

    public FiddlerCrabModel(ModelPart root) {
        this.root = root;
        ModelPart body_base = root.getChild("body_base");
        ModelPart claw_left_a = body_base.getChild("claw_left_a");
        ModelPart claw_left_c = claw_left_a.getChild("claw_left_c");
        claw_left_c_r1 = claw_left_c.getChild("claw_left_c_r1");
        leg_left_fra = body_base.getChild("leg_left_fra");
        leg_left_mia = body_base.getChild("leg_left_mia");
        leg_left_baa = body_base.getChild("leg_left_baa");
        leg_right_fra = body_base.getChild("leg_right_fra");
        leg_right_mia = body_base.getChild("leg_right_mia");
        leg_right_baa = body_base.getChild("leg_right_baa");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0f, -2.0f, -2.0f, 4.0f, 2.0f, 3.0f, false),
                PartPose.offsetAndRotation(0.0f, 23.0f, 0.0f, -7.5f * Mth.DEG_TO_RAD, 0.0f, 0.0f));

        body.addOrReplaceChild("eyestalk_right",
                CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(0.0f, -2.0f, 0.0f, 1.0f, 2.0f, 0.0f, false),
                PartPose.offsetAndRotation(-1.5f, -1.0f, -2.0f, 7.5f * Mth.DEG_TO_RAD, 0.0f, 0.0f));

        body.addOrReplaceChild("eyestalk_left",
                CubeListBuilder.create()
                        .texOffs(3, 21)
                        .addBox(-1.0f, -2.0f, 0.0f, 1.0f, 2.0f, 0.0f, false),
                PartPose.offsetAndRotation(1.5f, -1.0f, -2.0f, 7.5f * Mth.DEG_TO_RAD, 0.0f, 0.0f));

        PartDefinition modelPartClawRightA = body.addOrReplaceChild("claw_right_a",
                CubeListBuilder.create(),
                PartPose.offset(-2.0f, -0.5f, -2.0f));

        modelPartClawRightA.addOrReplaceChild("claw_right_a_r1",
                CubeListBuilder.create()
                        .texOffs(7, 6)
                        .addBox(-0.75f, -0.5f, -1.0f, 2.0f, 1.0f, 1.0f, false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 5.0f * Mth.DEG_TO_RAD, 12.5f * Mth.DEG_TO_RAD));

        PartDefinition modelPartClawLeftA = body.addOrReplaceChild("claw_left_a",
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .addBox(-1.0f, -1.0f, -1.0f, 2.0f, 2.0f, 1.0f, false),
                PartPose.offsetAndRotation(2.0f, -0.5f, -2.0f, 0.0f, -17.5f * Mth.DEG_TO_RAD, -7.5f * Mth.DEG_TO_RAD));

        PartDefinition modelPartClawLeftC = modelPartClawLeftA.addOrReplaceChild("claw_left_c",
                CubeListBuilder.create(),
                PartPose.offset(-1.0f, 0.0f, 0.0f));

        modelPartClawLeftC.addOrReplaceChild("claw_left_c_r1",
                CubeListBuilder.create()
                        .texOffs(14, 6)
                        .addBox(-2.0f, 0.0f, -0.99f, 2.0f, 1.0f, 1.0f, false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -10.0f * Mth.DEG_TO_RAD));

        modelPartClawLeftA.addOrReplaceChild("claw_left_b",
                CubeListBuilder.create()
                        .texOffs(15, 0)
                        .addBox(-3.0f, 0.0f, -1.0f, 3.0f, 1.0f, 1.0f, false),
                PartPose.offset(-1.0f, -1.0f, 0.0f));

        PartDefinition modelPartLegLeftFra = body.addOrReplaceChild("leg_left_fra",
                CubeListBuilder.create()
                        .texOffs(15, 17)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f, false),
                PartPose.offsetAndRotation(1.5f, 0.0f, -1.5f, -25.0f * Mth.DEG_TO_RAD, -7.5f * Mth.DEG_TO_RAD, -122.5f * Mth.DEG_TO_RAD));

        modelPartLegLeftFra.addOrReplaceChild("leg_left_frb",
                CubeListBuilder.create()
                        .texOffs(7, 13)
                        .addBox(-2.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f, false),
                PartPose.offset(-0.5f, 2.0f, 0.0f));

        PartDefinition modelPartLegLeftMia = body.addOrReplaceChild("leg_left_mia",
                CubeListBuilder.create()
                        .texOffs(10, 17)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f, false),
                PartPose.offsetAndRotation(1.5f, 0.0f, -0.5f, -2.5f * Mth.DEG_TO_RAD, 5.0f * Mth.DEG_TO_RAD, -120.0f * Mth.DEG_TO_RAD));

        modelPartLegLeftMia.addOrReplaceChild("leg_left_mib",
                CubeListBuilder.create()
                        .texOffs(0, 13)
                        .addBox(-2.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f, false),
                PartPose.offset(-0.5f, 2.0f, 0.0f));

        PartDefinition modelPartLegLeftBaa = body.addOrReplaceChild("leg_left_baa",
                CubeListBuilder.create()
                        .texOffs(5, 17)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f, false),
                PartPose.offsetAndRotation(1.5f, 0.0f, 0.5f, 37.5f * Mth.DEG_TO_RAD, 45.0f * Mth.DEG_TO_RAD, -92.5f * Mth.DEG_TO_RAD));

        modelPartLegLeftBaa.addOrReplaceChild("leg_left_bab",
                CubeListBuilder.create()
                        .texOffs(21, 10)
                        .addBox(-2.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f, false),
                PartPose.offset(-0.5f, 2.0f, 0.0f));

        PartDefinition modelPartLegRightFra = body.addOrReplaceChild("leg_right_fra",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f, false),
                PartPose.offsetAndRotation(-1.5f, 0.0f, -1.5f, -25.0f * Mth.DEG_TO_RAD, 7.5f * Mth.DEG_TO_RAD, 122.5f * Mth.DEG_TO_RAD));

        modelPartLegRightFra.addOrReplaceChild("leg_right_frb",
                CubeListBuilder.create()
                        .texOffs(14, 10)
                        .addBox(0.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f, false),
                PartPose.offset(0.5f, 2.0f, 0.0f));

        PartDefinition modelPartLegRightMia = body.addOrReplaceChild("leg_right_mia",
                CubeListBuilder.create()
                        .texOffs(19, 13)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f, false),
                PartPose.offsetAndRotation(-1.5f, 0.0f, -0.5f, -2.5f * Mth.DEG_TO_RAD, -5.0f * Mth.DEG_TO_RAD, 120.0f * Mth.DEG_TO_RAD));

        modelPartLegRightMia.addOrReplaceChild("leg_right_mib",
                CubeListBuilder.create()
                        .texOffs(7, 10)
                        .addBox(0.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f, false),
                PartPose.offset(0.5f, 2.0f, 0.0f));

        PartDefinition modelPartLegRightBaa = body.addOrReplaceChild("leg_right_baa",
                CubeListBuilder.create()
                        .texOffs(14, 13)
                        .addBox(-0.5f, 0.0f, -0.5f, 1.0f, 2.0f, 1.0f, false),
                PartPose.offsetAndRotation(-1.5f, 0.0f, 0.5f, 37.5f * Mth.DEG_TO_RAD, -45.0f * Mth.DEG_TO_RAD, 92.5f * Mth.DEG_TO_RAD));

        modelPartLegRightBaa.addOrReplaceChild("leg_right_bab",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(0.0f, -1.0f, -0.5f, 2.0f, 1.0f, 1.0f, false),
                PartPose.offset(0.5f, 2.0f, 0.0f));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.6f, limbSwingAmount)) {
            leg_right_fra.zRot += walk.eval(1.0f, 1.5f, 0.0f, 1.5f);
            leg_right_mia.zRot += walk.eval(1.0f, 1.5f, 0.4f, 1.5f);
            leg_right_baa.zRot += walk.eval(1.0f, 1.5f, 0.0f, 1.5f);

            leg_left_fra.zRot -= walk.eval(1.0f, 1.5f, 0.4f, 1.5f);
            leg_left_mia.zRot -= walk.eval(1.0f, 1.5f, 0.0f, 1.5f);
            leg_left_baa.zRot -= walk.eval(1.0f, 1.5f, 0.4f, 1.5f);
        }

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(age * 0.025f, 0.05f)) {
            claw_left_c_r1.zRot += idle.eval(1.0f, 1.0f, 0.0f, -0.5f);
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
