package dev.hikarishima.lightland.content.magic.products.instance;

import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.magic.products.MagicProduct;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.init.special.MagicRegistry;
import dev.lcy0x1.serial.NBTObj;
import net.minecraft.resources.ResourceLocation;

public class ArcaneMagic extends MagicProduct<Arcane, ArcaneMagic> {

	public ArcaneMagic(LLPlayerData player, NBTObj tag, ResourceLocation rl, IMagicRecipe<?> r) {
		super(MagicRegistry.MPT_ARCANE.get(), player, tag, rl, r);
	}

}
