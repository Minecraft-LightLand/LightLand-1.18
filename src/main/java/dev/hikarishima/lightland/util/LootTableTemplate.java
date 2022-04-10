package dev.hikarishima.lightland.util;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LootTableTemplate {

	public static LootPool.Builder getPool(int roll, int bonus) {
		return LootPool.lootPool().setRolls(ConstantValue.exactly(roll)).setBonusRolls(ConstantValue.exactly(0));
	}

	public static LootPoolSingletonContainer.Builder<?> getItem(Item item, int count) {
		return LootItem.lootTableItem(item)
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
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

	public static LootItemBlockStatePropertyCondition.Builder withBlockState(Block block, Property<Integer> prop, int val) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
				StatePropertiesPredicate.Builder.properties().hasProperty(prop, val)
		);
	}

	public static LootItemBlockStatePropertyCondition.Builder withBlockState(Block block, Property<Boolean> prop, boolean val) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
				StatePropertiesPredicate.Builder.properties().hasProperty(prop, val)
		);
	}

	public static LootItemBlockStatePropertyCondition.Builder withBlockState(Block block, Property<?> prop, String val) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
				StatePropertiesPredicate.Builder.properties().hasProperty(prop, val)
		);
	}

	public static LootPoolSingletonContainer.Builder<?> cropDrop(Item item) {
		return LootItem.lootTableItem(item).apply(ApplyBonusCount
				.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.57f, 3));
	}

}