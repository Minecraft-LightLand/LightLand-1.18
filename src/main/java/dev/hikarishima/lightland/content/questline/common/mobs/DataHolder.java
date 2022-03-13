package dev.hikarishima.lightland.content.questline.common.mobs;

import dev.lcy0x1.util.SerialClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;

public interface DataHolder<T extends DataHolder.EntityData> {

	@SerialClass
	class EntityData {

	}

	T getEmptyData();

	void defineSynchedData(T data);

	void onSyncedDataUpdated(T data, EntityDataAccessor<?> accessor);

	void addAdditionalSaveData(T data, CompoundTag tag);

	void readAdditionalSaveData(T data, CompoundTag tag);

}
