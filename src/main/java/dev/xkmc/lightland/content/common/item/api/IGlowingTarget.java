package dev.xkmc.lightland.content.common.item.api;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IGlowingTarget {

	@OnlyIn(Dist.CLIENT)
	int getDistance(ItemStack stack);

}
