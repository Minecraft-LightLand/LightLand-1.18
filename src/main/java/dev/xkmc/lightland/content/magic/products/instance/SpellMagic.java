package dev.xkmc.lightland.content.magic.products.instance;

import dev.xkmc.l2library.serial.NBTObj;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.magic.products.MagicProduct;
import dev.xkmc.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.lightland.content.magic.spell.internal.Spell;
import dev.xkmc.lightland.init.special.MagicRegistry;
import net.minecraft.resources.ResourceLocation;

public class SpellMagic extends MagicProduct<Spell<?, ?>, SpellMagic> {

	public SpellMagic(LLPlayerData player, NBTObj tag, ResourceLocation rl, IMagicRecipe<?> r) {
		super(MagicRegistry.MPT_SPELL.get(), player, tag, rl, r);
	}

}
