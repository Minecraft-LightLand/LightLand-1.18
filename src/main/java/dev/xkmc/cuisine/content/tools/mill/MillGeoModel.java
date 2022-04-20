package dev.xkmc.cuisine.content.tools.mill;

import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MillGeoModel extends AnimatedGeoModel<MillBlockEntity> {

	@Override
	public ResourceLocation getModelLocation(MillBlockEntity object) {
		return new ResourceLocation(Cuisine.MODID, "geo/mill.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(MillBlockEntity object) {
		return new ResourceLocation(Cuisine.MODID, "textures/gecko/mill.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation(MillBlockEntity animatable) {
		return new ResourceLocation(Cuisine.MODID, "animations/mill.animation.json");
	}
}
