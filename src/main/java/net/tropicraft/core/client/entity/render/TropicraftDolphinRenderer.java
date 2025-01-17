package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropicraftDolphinModel;
import net.tropicraft.core.common.entity.underdasea.TropicraftDolphinEntity;

@OnlyIn(Dist.CLIENT)
public class TropicraftDolphinRenderer extends MobRenderer<TropicraftDolphinEntity, TropicraftDolphinModel> {

    public TropicraftDolphinRenderer(EntityRendererProvider.Context context) {
        super(context, new TropicraftDolphinModel(context.bakeLayer(TropicraftRenderLayers.DOLPHIN_LAYER)), 0.5f);
        shadowStrength = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(TropicraftDolphinEntity dolphin) {
        return Tropicraft.location("textures/entity/" + dolphin.getTexture() + ".png");
    }
}
