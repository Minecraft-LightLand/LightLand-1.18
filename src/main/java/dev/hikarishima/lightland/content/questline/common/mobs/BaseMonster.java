package dev.hikarishima.lightland.content.questline.common.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class BaseMonster<T extends BaseMonster<T>> extends Monster {

	public record EntityConfig(MobType type, SoundPackage sound,
							   List<SpawnedEquipment> equipment,
							   List<DataHolder<?>> data,
							   Set<EntityType<?>> ally) {

	}

	private static final ThreadLocal<EntityConfig> TEMP = new ThreadLocal<>();

	private static <T extends BaseMonster<T>> EntityType<T> save(EntityType<T> type, EntityConfig config) {
		TEMP.set(config);
		return type;
	}

	private EntityConfig config;
	private final Map<DataHolder<?>, DataHolder.EntityData> data_map = new HashMap<>();

	protected BaseMonster(EntityType<T> type, Level level, EntityConfig config) {
		super(save(type, config), level);
		getConfig();
	}

	public final EntityConfig getConfig() {
		if (config == null) {
			config = TEMP.get();
			for (DataHolder<?> data : config.data) {
				data_map.put(data, data.getEmptyData());
			}
			TEMP.set(null);
		}
		return config;
	}

	@Override
	protected final SoundEvent getAmbientSound() {
		return getConfig().sound.ambient();
	}

	@Override
	protected final SoundEvent getHurtSound(DamageSource source) {
		return getConfig().sound.hurt();
	}

	@Override
	protected final SoundEvent getDeathSound() {
		return getConfig().sound.death();
	}

	@Override
	protected final void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(getConfig().sound.step(), 0.15F, 1.0F);
	}

	@Override
	public final MobType getMobType() {
		return getConfig().type;
	}

	@Override
	public final double getMyRidingOffset() {
		return this.isBaby() ? 0.0D : -0.45D;
	}

	@Override
	protected final void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(difficulty);
		for (SpawnedEquipment e : getConfig().equipment())
			e.populateDefaultEquipmentSlots(this, difficulty);
	}

	@Override
	protected final void populateDefaultEquipmentEnchantments(DifficultyInstance difficulty) {
		super.populateDefaultEquipmentEnchantments(difficulty);
		for (SpawnedEquipment e : getConfig().equipment())
			e.populateDefaultEquipmentEnchantments(this, difficulty);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
											  @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
		this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficulty.getSpecialMultiplier());
		for (SpawnedEquipment e : getConfig().equipment())
			groupData = e.finalizeSpawn(this, level, difficulty, spawnType, groupData, tag);
		return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
	}

	@Override
	public final void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		for (DataHolder<?> holder : getConfig().data) {
			caller(holder, (h, data) -> h.addAdditionalSaveData(data, tag));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		for (DataHolder<?> holder : getConfig().data) {
			caller(holder, (h, data) -> h.readAdditionalSaveData(data, tag));
		}
	}

	@Override
	protected final void defineSynchedData() {
		super.defineSynchedData();
		for (DataHolder<?> holder : getConfig().data) {
			caller(holder, DataHolder::defineSynchedData);
		}
	}

	@Override
	public final void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		for (DataHolder<?> holder : getConfig().data) {
			caller(holder, (h, data) -> h.onSyncedDataUpdated(data, accessor));
		}
		super.onSyncedDataUpdated(accessor);
	}

	@Override
	public boolean canAttackType(EntityType<?> type) {
		return !getConfig().ally.contains(type) && super.canAttackType(type);
	}

	@Override
	public final Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@SuppressWarnings({"unchecked", "unsafe"})
	protected final <D extends DataHolder.EntityData> D getData(DataHolder<D> holder) {
		return (D) data_map.get(holder);
	}

	protected final <D extends DataHolder.EntityData> void caller(DataHolder<D> holder, BiConsumer<DataHolder<D>, D> action) {
		action.accept(holder, getData(holder));
	}

}
