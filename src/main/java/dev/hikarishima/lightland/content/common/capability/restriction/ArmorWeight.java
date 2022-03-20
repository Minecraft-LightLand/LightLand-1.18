package dev.hikarishima.lightland.content.common.capability.restriction;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.network.config.ConfigSyncManager;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;

@SerialClass
public class ArmorWeight extends ConfigSyncManager.BaseConfig {

	@Nullable
	public static ArmorWeight getInstance() {
		return (ArmorWeight) ConfigSyncManager.CONFIGS.get("lightland:config_weight");
	}

	public static boolean canPutOn(Player player, ItemStack stack) {
		if (player == null || !LLPlayerData.isProper(player))
			return true;
		ArmorWeight weight = getInstance();
		if (weight == null)
			return true;
		int ans = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
			ItemStack armor = player.getItemBySlot(slot);
			if (armor.isEmpty()) continue;
			if (slot == ((ArmorItem) stack.getItem()).getSlot()) continue;
			ans += getWeight(armor);
		}
		ans += getWeight(stack);
		return ans <= LLPlayerData.get(player).abilityPoints.getWeightAble();
	}

	public static int getArmorWeight(Player player) {
		ArmorWeight weight = getInstance();
		if (weight == null)
			return 0;
		int ans = 0;
		for (ItemStack stack : player.getArmorSlots()) {
			ans += getWeight(stack);
		}
		return ans;
	}

	@SerialClass.SerialField
	private HashMap<String, Entry> entries = new HashMap<>();

	@SerialClass.SerialField
	public HashMap<String, String> materials = new HashMap<>();

	@SerialClass.SerialField
	public String[] suffixes;

	@SerialClass
	public static class Entry {

		@SerialClass.SerialField
		public int ingredient_weight;

		@SerialClass.SerialField
		public int extra_weight;

	}

	public static int getWeight(ItemStack stack) {
		ArmorWeight ins = getInstance();
		if (ins == null) return 0;
		int weight = ins.getItemWeight(stack);
		int lv = 0;//EnchantmentHelper.getItemEnchantmentLevel(VanillaMagicRegistrate.ENCH_HEAVY.get(), stack);
		return (int) (weight * (1 + 0.1 * lv));
	}

	private int getItemWeight(ItemStack stack) {
		if (stack.getItem() instanceof ArmorItem armor) {
			int slot_factor = getSlotFactor(armor.getSlot());
			int weight_factor = 0;
			int weight_extra = 0;
			if (armor.getRegistryName() != null) {
				String id = armor.getRegistryName().toString();
				ArmorWeight.Entry entry = entries.get(id);
				if (entry == null) {
					String cut = cut(id);
					id = materials.get(id);
					if (id == null)
						id = cut;
					entry = entries.get(id);
				}
				if (entry != null) {
					weight_factor = entry.ingredient_weight;
					weight_extra = entry.extra_weight;
				}
			}
			int weight = slot_factor * weight_factor + weight_extra;
			if (weight > 0)
				return weight;
			return armor.getDefense() * 100;
		}
		return 0;
	}

	private String cut(String id) {
		for (String suf : suffixes) {
			if (id.endsWith("_" + suf))
				return id.substring(0, id.length() - 1 - suf.length());
		}
		return id;
	}

	public static int getSlotFactor(EquipmentSlot slot) {
		if (slot == EquipmentSlot.HEAD) {
			return 5;
		} else if (slot == EquipmentSlot.CHEST) {
			return 8;
		} else if (slot == EquipmentSlot.LEGS) {
			return 7;
		} else if (slot == EquipmentSlot.FEET) {
			return 4;
		}
		return 0;
	}

}
