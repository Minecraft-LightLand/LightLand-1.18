package dev.hikarishima.lightland.content.common.capability.restriction;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class ArmorEnchant {

	public static final double FACTOR = 0.25;

	public static int getArmorEnchantLevel(Player player) {
		double ans = 0;
		for (ItemStack stack : player.getArmorSlots()) {
			ans += getItemArmorEnchantLevel(stack);
		}
		return (int) Math.ceil(ans);
	}

	public static double getItemArmorEnchantLevel(ItemStack stack) {
		double ans = 0;
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
			if (!entry.getKey().isCurse()) {
				double factor = FACTOR;
				factor *= entry.getKey().isTreasureOnly() ? 2 : 1;
				factor *= entry.getKey().getMaxLevel() == 1 ? 2 : 1;
				ans += entry.getValue() * factor;
			}
		}
		return ans;
	}

}
