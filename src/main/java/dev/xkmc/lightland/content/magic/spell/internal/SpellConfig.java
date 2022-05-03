package dev.xkmc.lightland.content.magic.spell.internal;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.magic.item.MagicScroll;
import dev.xkmc.lightland.content.magic.products.MagicProduct;
import dev.xkmc.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.lightland.init.special.MagicRegistry;
import dev.xkmc.lightland.network.config.SpellDataConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SerialClass
public class SpellConfig {

	@SerialClass.SerialField
	public int duration, mana_cost, spell_load;
	@SerialClass.SerialField
	public float factor = 1f;

	@SerialClass.SerialField
	public MagicScroll.ScrollType type;

	public static <C extends SpellConfig> C get(Spell<C, ?> spell, Level world, Player player) {
		C ans = SpellDataConfig.getConfig(spell.getRegistryName());

		IMagicRecipe<?> r = IMagicRecipe.getMap(world, MagicRegistry.MPT_SPELL.get()).get(spell);
		if (r == null)
			return ans;
		MagicProduct<?, ?> p = LLPlayerData.get(player).magicHolder.getProduct(r);
		if (p == null || !p.usable())
			return ans;
		ans = makeCopy(ans);
		ans.mana_cost += p.getCost() * ans.factor;
		ans.spell_load += p.getCost() * ans.factor;

		return ans;
	}

	public static <C extends SpellConfig> C makeCopy(C config) {
		return TagCodec.fromTag(TagCodec.toTag(new CompoundTag(), config), config.getClass());
	}

	@SerialClass
	public static class SpellDisplay {

		@SerialClass.SerialField
		public String id;

		@SerialClass.SerialField
		public int duration, setup, close;

	}

}
