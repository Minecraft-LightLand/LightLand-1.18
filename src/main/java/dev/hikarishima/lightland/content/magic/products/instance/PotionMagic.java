package dev.hikarishima.lightland.content.magic.products.instance;

import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.magic.products.MagicProduct;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.init.special.MagicRegistry;
import dev.lcy0x1.serial.NBTObj;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class PotionMagic extends MagicProduct<MobEffect, PotionMagic> {

	public PotionMagic(LLPlayerData player, NBTObj tag, ResourceLocation rl, IMagicRecipe<?> r) {
		super(MagicRegistry.MPT_EFF.get(), player, tag, rl, r);
	}
}
