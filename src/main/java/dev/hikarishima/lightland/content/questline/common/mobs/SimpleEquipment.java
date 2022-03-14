package dev.hikarishima.lightland.content.questline.common.mobs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SimpleEquipment implements SpawnedEquipment {

	private final WeightedRandomList<WeightedEntry.Wrapper<Item>> list;

	private final EquipmentSlot slot;
	private final double chance;
	private final int level_1, level_2;

	public SimpleEquipment(EquipmentSlot slot, WeightedRandomList<WeightedEntry.Wrapper<Item>> list, double chance, int level_1, int level_2) {
		this.list = list;
		this.slot = slot;
		this.chance = chance;
		this.level_1 = level_1;
		this.level_2 = level_2;
	}

	@Override
	public void populateDefaultEquipmentSlots(BaseMonster<?> entity, DifficultyInstance difficulty) {
		if (entity.hasItemInSlot(slot)) return;
		if (entity.getRandom().nextFloat() > chance) return;
		Optional<WeightedEntry.Wrapper<Item>> op = list.getRandom(entity.getRandom());
		if (op.isPresent()) {
			Item e = op.get().getData();
			entity.setItemSlot(slot, new ItemStack(e));
		}
	}

	@Override
	public void populateDefaultEquipmentEnchantments(BaseMonster<?> entity, DifficultyInstance difficulty) {
		ItemStack stack = entity.getItemBySlot(slot);
		if (!stack.isEmpty() && stack.isEnchantable() && !stack.isEnchanted()) {
			int level = (int) (level_1 + entity.getRandom().nextFloat() * (level_2 - level_1));
			EnchantmentHelper.enchantItem(entity.getRandom(), stack, level, false);
		}
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(BaseMonster<?> entity, ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
		return groupData;
	}
}
