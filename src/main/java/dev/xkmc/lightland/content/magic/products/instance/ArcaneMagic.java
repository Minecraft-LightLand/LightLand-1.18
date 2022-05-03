package dev.xkmc.lightland.content.magic.products.instance;

import dev.xkmc.l2library.serial.NBTObj;
import dev.xkmc.lightland.content.arcane.internal.Arcane;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.magic.products.MagicProduct;
import dev.xkmc.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.lightland.init.special.MagicRegistry;
import net.minecraft.resources.ResourceLocation;

public class ArcaneMagic extends MagicProduct<Arcane, ArcaneMagic> {

	public ArcaneMagic(LLPlayerData player, NBTObj tag, ResourceLocation rl, IMagicRecipe<?> r) {
		super(MagicRegistry.MPT_ARCANE.get(), player, tag, rl, r);
	}

}
