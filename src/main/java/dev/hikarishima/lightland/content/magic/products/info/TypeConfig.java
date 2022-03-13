package dev.hikarishima.lightland.content.magic.products.info;

import dev.lcy0x1.util.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class TypeConfig {

	@SerialClass.SerialField
	public Item icon;

	@SerialClass.SerialField
	public ResourceLocation background;

	public ItemStack getIcon() {
		return icon.getDefaultInstance();
	}

	public ResourceLocation getBackground() {
		return background;
	}

}
