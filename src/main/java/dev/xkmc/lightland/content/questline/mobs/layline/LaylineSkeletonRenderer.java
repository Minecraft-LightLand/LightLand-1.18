package dev.xkmc.lightland.content.questline.mobs.layline;

import dev.xkmc.lightland.init.LightLand;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class LaylineSkeletonRenderer extends HumanoidMobRenderer<LaylineSkeleton, LaylineSkeletonModel> {

	public LaylineSkeletonRenderer(EntityRendererProvider.Context p_174380_) {
		this(p_174380_, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
	}

	public LaylineSkeletonRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation body, ModelLayerLocation armorIn, ModelLayerLocation armorOut) {
		super(ctx, new LaylineSkeletonModel(ctx.bakeLayer(body)), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this,
				new LaylineSkeletonModel(ctx.bakeLayer(armorIn)),
				new LaylineSkeletonModel(ctx.bakeLayer(armorOut))));
	}

	public ResourceLocation getTextureLocation(LaylineSkeleton entity) {
		return new ResourceLocation(LightLand.MODID, "textures/entity/mob/layline_skeleton.png");
	}

}