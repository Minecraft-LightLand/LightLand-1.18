package dev.hikarishima.lightland.content.questline.common.mobs;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LootTableTemplate {

	public static LootPool.Builder getPool(int roll, int bonus) {
		return LootPool.lootPool().setRolls(ConstantValue.exactly(roll)).setBonusRolls(ConstantValue.exactly(0));
	}

	public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int min, int max) {
		return LootItem.lootTableItem(item)
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
	}

	public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int min, int max, int add) {
		return LootItem.lootTableItem(item)
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
				.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, add)));
	}

	public static LootItemCondition.Builder byPlayer() {
		return LootItemKilledByPlayerCondition.killedByPlayer();
	}

	public static LootItemCondition.Builder chance(float chance) {
		return LootItemRandomChanceCondition.randomChance(chance);
	}

	public static LootItemCondition.Builder chance(float chance, float add) {
		return LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(chance, add);
	}
}
