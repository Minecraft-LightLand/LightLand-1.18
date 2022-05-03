package dev.xkmc.lightland.content.arcane.internal;

import dev.xkmc.lightland.init.special.LightLandRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArcaneItemCraftHelper {

	public static Arcane getArcaneOnItem(ItemStack stack, ArcaneType type) {
		CompoundTag tag = stack.getOrCreateTagElement("arcane");
		String s = type.getID();
		if (!tag.contains(s))
			return null;
		String str = tag.getString(s);
		ResourceLocation rl = new ResourceLocation(str);
		return LightLandRegistry.ARCANE.getValue(rl);
	}

	public static List<Arcane> getAllArcanesOnItem(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTagElement("arcane");
		List<Arcane> list = new ArrayList<>();
		for (String str : tag.getAllKeys()) {
			if (str.equals("charged") || str.equals("mana"))
				continue;
			list.add(LightLandRegistry.ARCANE.getValue(new ResourceLocation(tag.getString(str))));
		}
		return list;
	}

	public static void setArcaneOnItem(ItemStack stack, Arcane arcane) {
		String s = arcane.type.get().getID();
		String str = arcane.getID();
		stack.getOrCreateTagElement("arcane").putString(s, str);
	}

}
