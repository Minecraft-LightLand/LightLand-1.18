package dev.xkmc.lightland.content.questline.common.mobs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public interface SpawnedEquipment {

	void populateDefaultEquipmentSlots(BaseMonster<?> entity, DifficultyInstance difficulty);

	void populateDefaultEquipmentEnchantments(BaseMonster<?> entity, DifficultyInstance difficulty);

	@Nullable
	SpawnGroupData finalizeSpawn(BaseMonster<?> entity, ServerLevelAccessor level, DifficultyInstance difficulty,
								 MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag);

}
