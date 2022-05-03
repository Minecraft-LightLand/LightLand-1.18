package dev.xkmc.lightland.content.magic.ritual;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.content.magic.block.RitualCore;
import dev.xkmc.lightland.init.registrate.LightlandRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@SerialClass
@ParametersAreNonnullByDefault
public class EnchantRitualRecipe extends AbstractLevelRitualRecipe<EnchantRitualRecipe> {

	public EnchantRitualRecipe(ResourceLocation id) {
		super(id, LightlandRecipe.RS_ENCH.get());
	}

	public void assemble(RitualCore.Inv inv, int level) {
		ItemStack stack = assemble(inv);
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		map.replaceAll((e, v) -> v + level - 1);
		EnchantmentHelper.setEnchantments(map, stack);
		inv.setItem(5, stack);
	}

}
