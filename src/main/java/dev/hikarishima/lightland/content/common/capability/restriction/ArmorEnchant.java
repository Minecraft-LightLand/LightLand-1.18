package dev.hikarishima.lightland.content.common.capability.restriction;

import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.common.capability.player.MagicAbility;
import dev.hikarishima.lightland.network.config.ConfigSyncManager;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.HashMap;
import java.util.Map;

@SerialClass
public class ArmorEnchant extends ConfigSyncManager.BaseConfig {

	private static ArmorEnchant getInstance() {
		return (ArmorEnchant) ConfigSyncManager.CONFIGS.get("lightland:config_enchant");
	}

	public static int getArmorEnchantLevel(Player player) {
		int ans = 0;
		for (ItemStack stack : player.getArmorSlots()) {
			ans += getItemArmorEnchantLevel(stack);
		}
		return ans;
	}

	public static boolean canPutOn(AbstractClientPlayer player, ItemStack stack) {
		if (player == null || !LLPlayerData.isProper(player))
			return true;
		ArmorEnchant enchant = getInstance();
		if (enchant == null)
			return true;
		int ans = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
			ItemStack armor = player.getItemBySlot(slot);
			if (armor.isEmpty()) continue;
			if (slot == ((ArmorItem) stack.getItem()).getSlot()) continue;
			ans += getItemArmorEnchantLevel(armor);
		}
		ans += getItemArmorEnchantLevel(stack);
		MagicAbility ab = LLPlayerData.get(player).magicAbility;
		return ans <= (ab.getManaRestoration() + ab.getSpellReduction()) * MagicAbility.ENCHANT_FACTOR;
	}

	public static int getItemArmorEnchantLevel(ItemStack stack) {
		if (getInstance() == null)
			return 0;
		int ans = 0;
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
			if (!entry.getKey().isCurse()) {
				int factor = getInstance().map.getOrDefault(entry.getKey(), 0);
				ans += entry.getValue() * factor;
			}
		}
		int affinity = stack.getItem().getEnchantmentValue();
		return ans < affinity ? ans : affinity + (ans - affinity) * 10;
	}

	public static boolean isCursed(ItemStack stack) {
		if (getInstance() == null)
			return false;
		int ans = 0;
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
			if (!entry.getKey().isCurse()) {
				int factor = getInstance().map.getOrDefault(entry.getKey(), 0);
				ans += entry.getValue() * factor;
			}
		}
		int affinity = stack.getItem().getEnchantmentValue();
		return ans > affinity;
	}

	@SerialClass.SerialField
	public HashMap<Enchantment, Integer> map = new HashMap<>();

}
