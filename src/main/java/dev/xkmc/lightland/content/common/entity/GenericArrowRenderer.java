package dev.xkmc.lightland.content.common.entity;

import dev.xkmc.lightland.content.archery.item.GenericArrowItem;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GenericArrowRenderer extends ArrowRenderer<GenericArrowEntity> {

	public GenericArrowRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public ResourceLocation getTextureLocation(GenericArrowEntity entity) {
		GenericArrowItem arrow = entity.data.arrow().item();
		return new ResourceLocation(arrow.getRegistryName().getNamespace(), "textures/entity/arrow/" + arrow.getRegistryName().getPath() + ".png");
	}

}
