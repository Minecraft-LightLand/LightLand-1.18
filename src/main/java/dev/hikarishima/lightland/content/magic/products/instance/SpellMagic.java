package dev.hikarishima.lightland.content.magic.products.instance;

import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.magic.products.MagicProduct;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.content.magic.spell.internal.Spell;
import dev.hikarishima.lightland.init.special.MagicRegistry;
import dev.xkmc.l2library.serial.NBTObj;
import net.minecraft.resources.ResourceLocation;

public class SpellMagic extends MagicProduct<Spell<?, ?>, SpellMagic> {

	public SpellMagic(LLPlayerData player, NBTObj tag, ResourceLocation rl, IMagicRecipe<?> r) {
		super(MagicRegistry.MPT_SPELL.get(), player, tag, rl, r);
	}

}
