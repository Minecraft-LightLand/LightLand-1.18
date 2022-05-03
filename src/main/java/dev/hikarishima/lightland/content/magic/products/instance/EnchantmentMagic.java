package dev.hikarishima.lightland.content.magic.products.instance;

import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.magic.products.MagicProduct;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.init.special.MagicRegistry;
import dev.xkmc.l2library.serial.NBTObj;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentMagic extends MagicProduct<Enchantment, EnchantmentMagic> {

	public EnchantmentMagic(LLPlayerData player, NBTObj nbtManager, ResourceLocation rl, IMagicRecipe<?> r) {
		super(MagicRegistry.MPT_ENCH.get(), player, nbtManager, rl, r);
	}

}
