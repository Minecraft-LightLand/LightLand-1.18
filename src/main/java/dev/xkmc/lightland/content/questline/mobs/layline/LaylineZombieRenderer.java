package dev.xkmc.lightland.content.questline.mobs.layline;

import dev.xkmc.lightland.init.LightLand;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class LaylineZombieRenderer extends HumanoidMobRenderer<LaylineZombie, LaylineZombieModel> {

	public LaylineZombieRenderer(EntityRendererProvider.Context ctx) {
		this(ctx, new LaylineZombieModel(ctx.bakeLayer(ModelLayers.ZOMBIE)),
				new LaylineZombieModel(ctx.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
				new LaylineZombieModel(ctx.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)));
	}

	protected LaylineZombieRenderer(EntityRendererProvider.Context ctx, LaylineZombieModel body, LaylineZombieModel armorIn, LaylineZombieModel armorOut) {
		super(ctx, body, 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, armorIn, armorOut));
	}

	@Override
	public ResourceLocation getTextureLocation(LaylineZombie entity) {
		return new ResourceLocation(LightLand.MODID, "textures/entity/mob/layline_zombie.png");
	}
}
