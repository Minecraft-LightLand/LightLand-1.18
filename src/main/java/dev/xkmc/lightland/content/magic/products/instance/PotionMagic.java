package dev.xkmc.lightland.content.magic.products.instance;

import dev.xkmc.l2library.serial.NBTObj;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.magic.products.MagicProduct;
import dev.xkmc.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.lightland.init.special.MagicRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class PotionMagic extends MagicProduct<MobEffect, PotionMagic> {

	public PotionMagic(LLPlayerData player, NBTObj tag, ResourceLocation rl, IMagicRecipe<?> r) {
		super(MagicRegistry.MPT_EFF.get(), player, tag, rl, r);
	}
}
