package dev.xkmc.lightland.content.arcane.internal;

import dev.xkmc.lightland.content.common.item.api.IGlowingTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IArcaneItem extends IGlowingTarget {

	int getMaxMana(ItemStack stack);

	@OnlyIn(Dist.CLIENT)
	default int getDistance(ItemStack stack) {
		return 64;
	}

}
