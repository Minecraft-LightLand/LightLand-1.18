package dev.xkmc.lightland.content.magic.products;

import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.lightland.init.special.LightLandRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MagicElement extends NamedEntry<MagicElement> {

	public final int color;

	public MagicElement(int color) {
		super(() -> LightLandRegistry.ELEMENT);
		this.color = color;
	}

	public String getName() {
		String domain = getRegistryName().getNamespace();
		String name = getRegistryName().getPath();
		return "magic_element." + domain + "." + name;
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getIcon() {
		ResourceLocation rl = getRegistryName();
		return new ResourceLocation(rl.getNamespace(), "textures/magic_elements/" + rl.getPath() + ".png");
	}

}
