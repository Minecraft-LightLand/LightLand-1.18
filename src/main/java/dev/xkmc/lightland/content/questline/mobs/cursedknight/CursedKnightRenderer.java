package dev.xkmc.lightland.content.questline.mobs.cursedknight;

import dev.xkmc.lightland.init.LightLand;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class CursedKnightRenderer extends HumanoidMobRenderer<BaseCursedKnight<?>, CursedKnightModel> {

	public CursedKnightRenderer(EntityRendererProvider.Context ctx) {
		this(ctx, new CursedKnightModel(ctx.bakeLayer(ModelLayers.ZOMBIE)),
				new CursedKnightModel(ctx.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
				new CursedKnightModel(ctx.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)));
	}

	protected CursedKnightRenderer(EntityRendererProvider.Context ctx, CursedKnightModel body, CursedKnightModel armorIn, CursedKnightModel armorOut) {
		super(ctx, body, 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, armorIn, armorOut));
	}

	@Override
	public ResourceLocation getTextureLocation(BaseCursedKnight<?> entity) {
		return new ResourceLocation(LightLand.MODID, "textures/entity/mob/layline_zombie.png");
	}
}
