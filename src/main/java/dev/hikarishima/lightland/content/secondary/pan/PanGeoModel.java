package dev.hikarishima.lightland.content.secondary.pan;

import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PanGeoModel extends AnimatedGeoModel<PanBlockEntity> {

	@Override
	public ResourceLocation getModelLocation(PanBlockEntity object) {
		return new ResourceLocation(LightLand.MODID, "geo/saucepan.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(PanBlockEntity object) {
		return new ResourceLocation(LightLand.MODID, "gecko/saucepan.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation(PanBlockEntity animatable) {
		return new ResourceLocation(LightLand.MODID, "animations/saucepan.animation.json");
	}
}
