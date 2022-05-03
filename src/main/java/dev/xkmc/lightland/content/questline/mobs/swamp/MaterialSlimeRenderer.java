package dev.xkmc.lightland.content.questline.mobs.swamp;

import dev.xkmc.lightland.init.LightLand;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;

public class MaterialSlimeRenderer extends SlimeRenderer {

	public MaterialSlimeRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Slime slime) {
		if (slime instanceof MaterialSlime<?>) {
			return new ResourceLocation(LightLand.MODID, "textures/entity/slime/" + slime.getType().getRegistryName().getPath() + ".png");
		}
		return super.getTextureLocation(slime);
	}
}
