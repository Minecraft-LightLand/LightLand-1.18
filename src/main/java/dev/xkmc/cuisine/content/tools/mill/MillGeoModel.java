package dev.xkmc.cuisine.content.tools.mill;

import dev.hikarishima.lightland.init.LightLand;
import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MillGeoModel extends AnimatedGeoModel<MillBlockEntity> {

	@Override
	public ResourceLocation getModelLocation(MillBlockEntity object) {
		return new ResourceLocation(LightLand.MODID, "geo/mill.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(MillBlockEntity object) {
		return new ResourceLocation(LightLand.MODID, "textures/gecko/mill.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation(MillBlockEntity animatable) {
		return new ResourceLocation(LightLand.MODID, "animations/mill.animation.json");
	}
}
