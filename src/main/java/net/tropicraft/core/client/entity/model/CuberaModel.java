package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.underdasea.CuberaEntity;

public class CuberaModel<T extends CuberaEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart fin_anal;
    private final ModelPart fin_pelvic_right_r1;
    private final ModelPart fin_pelvic_left_r1;
    private final ModelPart fin_pectoral_left;
    private final ModelPart fin_pectoral_right;
    private final ModelPart fin_dorsal;
    private final ModelPart jaw_lower;
    private final ModelPart tail_base;
    private final ModelPart tail_main;
    private final ModelPart fin_tail;

    public CuberaModel(ModelPart root) {
        this.root = root;
        fin_anal = root.getChild("fin_anal");
        ModelPart fin_pelvic_right = root.getChild("fin_pelvic_right");
        fin_pelvic_right_r1 = fin_pelvic_right.getChild("fin_pelvic_right_r1");
        ModelPart fin_pelvic_left = root.getChild("fin_pelvic_left");
        fin_pelvic_left_r1 = fin_pelvic_left.getChild("fin_pelvic_left_r1");
        fin_pectoral_left = root.getChild("fin_pectoral_left");
        fin_pectoral_right = root.getChild("fin_pectoral_right");
        fin_dorsal = root.getChild("fin_dorsal");
        ModelPart body_connection = root.getChild("body_connection");
        jaw_lower = body_connection.getChild("jaw_lower");
        tail_base = root.getChild("tail_base");
        tail_main = tail_base.getChild("tail_main");
        fin_tail = tail_main.getChild("fin_tail");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body_base", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.0f, -5.0f, -3.0f, 4.0f, 6.0f, 8.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0436f, 0.0f, 0.0f));

        root.addOrReplaceChild("fin_anal", CubeListBuilder.create()
                        .texOffs(11, 37).addBox(0.0f, -1.0f, -1.0f, 0.0f, 2.0f, 4.0f),
                PartPose.offsetAndRotation(0.0f, 1.0f, 4.0f, -0.3054f, 0.0f, 0.0f));

        PartDefinition partDefinition2 = root.addOrReplaceChild("fin_pelvic_right", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-1.5f, 1.0f, -2.0f, 0.0f, 0.0f, 0.0f));

        partDefinition2.addOrReplaceChild("fin_pelvic_right_r1", CubeListBuilder.create()
                        .texOffs(20, 37).addBox(0.0f, -1.5f, -0.5f, 0.0f, 2.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -1.3526f, -0.2182f, 0.0f));

        PartDefinition partDefinition4 = root.addOrReplaceChild("fin_pelvic_left", CubeListBuilder.create(),
                PartPose.offsetAndRotation(1.5f, 1.0f, -2.0f, 0.0f, 0.0f, 0.0f));

        partDefinition4.addOrReplaceChild("fin_pelvic_left_r1", CubeListBuilder.create()
                        .texOffs(27, 37).addBox(0.0f, -1.5f, -0.5f, 0.0f, 2.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -1.3526f, 0.2182f, 0.0f));

        root.addOrReplaceChild("fin_pectoral_left", CubeListBuilder.create()
                        .texOffs(7, 45).addBox(0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 3.0f),
                PartPose.offsetAndRotation(2.0f, -1.0f, -2.0f, 0.4363f, 0.5672f, 0.0f));

        root.addOrReplaceChild("fin_pectoral_right", CubeListBuilder.create()
                        .texOffs(0, 45).addBox(0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 3.0f),
                PartPose.offsetAndRotation(-2.0f, -1.0f, -2.0f, 0.4363f, -0.5672f, 0.0f));

        root.addOrReplaceChild("fin_dorsal", CubeListBuilder.create()
                        .texOffs(25, 0).addBox(0.0f, -3.0f, 0.0f, 0.0f, 3.0f, 7.0f),
                PartPose.offsetAndRotation(0.0f, -5.0f, -1.0f, -0.3054f, 0.0f, 0.0f));

        PartDefinition partDefinition9 = root.addOrReplaceChild("body_connection", CubeListBuilder.create()
                        .texOffs(28, 15).addBox(-2.0f, -2.0f, -4.0f, 4.0f, 2.0f, 4.0f),
                PartPose.offsetAndRotation(0.0f, 1.0f, -3.0f, 0.0f, 0.0f, 0.0f));

        partDefinition9.addOrReplaceChild("jaw_lower", CubeListBuilder.create()
                        .texOffs(15, 29).addBox(-2.0f, -1.0f, -3.0f, 4.0f, 1.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, -4.0f, -0.1309f, 0.0f, 0.0f));

        PartDefinition partDefinition11 = root.addOrReplaceChild("head_base", CubeListBuilder.create()
                        .texOffs(0, 15).addBox(-2.0f, 0.0f, -4.0f, 4.0f, 4.0f, 4.0f, new CubeDeformation(0.01f)),
                PartPose.offsetAndRotation(0.0f, -5.0f, -3.0f, 0.4363f, 0.0f, 0.0f));

        PartDefinition partDefinition12 = partDefinition11.addOrReplaceChild("head_snout", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, 0.0f, -4.0f, 0.3054f, 0.0f, 0.0f));

        partDefinition12.addOrReplaceChild("head_snout_r1", CubeListBuilder.create()
                        .texOffs(0, 29).addBox(-2.0f, 0.0f, -3.0f, 4.0f, 3.0f, 3.0f, new CubeDeformation(0.01f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.1309f, 0.0f, 0.0f));

        PartDefinition partDefinition14 = root.addOrReplaceChild("tail_base", CubeListBuilder.create()
                        .texOffs(0, 37).addBox(-1.5f, 0.0f, 0.0f, 3.0f, 5.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, -5.5f, 5.0f, -0.0436f, 0.0f, 0.0f));

        PartDefinition partDefinition15 = partDefinition14.addOrReplaceChild("tail_main", CubeListBuilder.create()
                        .texOffs(30, 29).addBox(-1.0f, 0.0f, 0.0f, 2.0f, 4.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.5f, 2.0f, 0.0f, 0.0f, 0.0f));

        partDefinition15.addOrReplaceChild("fin_tail", CubeListBuilder.create()
                        .texOffs(17, 15).addBox(0.0f, -2.0f, -1.0f, 0.0f, 8.0f, 5.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        if (entity.isInWater()) {
            root.zRot = 0.0f;
            root.y = 20.0f;
        } else {
            root.zRot = 90.0f * ModelAnimator.DEG_TO_RAD;
            root.y = 22.0f;
        }

        try (ModelAnimator.Cycle swim = ModelAnimator.cycle(limbSwing * 0.4f, limbSwingAmount)) {
            tail_base.yRot = swim.eval(1.0f, 1.0f, 0.0f, 0.0f);
            tail_main.yRot = swim.eval(1.0f, 1.0f, 0.25f, 0.0f);
            fin_tail.yRot = swim.eval(1.0f, 1.0f, 0.5f, 0.0f);

            fin_dorsal.yRot = swim.eval(1.0f, 0.125f);
            fin_anal.yRot = swim.eval(1.0f, 0.125f);
        }

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(age * 0.05f, 0.1f)) {
            root.y += idle.eval(0.5f, 2.0f);

            jaw_lower.xRot = -7.5f * ModelAnimator.DEG_TO_RAD - idle.eval(1.0f, 0.5f, 0.0f, 1.0f);

            fin_pectoral_left.yRot = 32.5f * ModelAnimator.DEG_TO_RAD + idle.eval(0.5f, 1.0f);
            fin_pectoral_right.yRot = -32.5f * ModelAnimator.DEG_TO_RAD + idle.eval(0.5f, -1.0f);

            fin_pelvic_left_r1.zRot = idle.eval(0.5f, -1.0f, 0.2f, 0.0f);
            fin_pelvic_right_r1.zRot = idle.eval(0.5f, 1.0f, 0.2f, 0.0f);
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
