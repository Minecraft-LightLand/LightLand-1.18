package dev.hikarishima.lightland.content.common.capability.worldstorage;

import dev.lcy0x1.util.SerialClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@SerialClass
public class WorldStorage {

	public static Capability<WorldStorage> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static WorldStorage get(ServerLevel level) {
		return level.getServer().overworld().getCapability(CAPABILITY).resolve().get();
	}


	public final ServerLevel level;

	@SerialClass.SerialField
	private final HashMap<String, CompoundTag> storage = new HashMap<>();

	private final HashMap<UUID, StorageContainer> cache = new HashMap<>();

	public WorldStorage(ServerLevel level) {
		this.level = level;
	}

	public Optional<StorageContainer> getOrCreateStorage(UUID id, int color, long password) {
		if (cache.containsKey(id)) {
			StorageContainer storage = cache.get(id);
			if (storage.password == password)
				return Optional.of(storage);
			return Optional.empty();
		}
		CompoundTag col = getCol(id, color, password);
		if (col.getLong("password") != password)
			return Optional.empty();
		StorageContainer storage = new StorageContainer(id, col);
		cache.put(id, storage);
		return Optional.of(storage);
	}

	public StorageContainer changePassword(UUID id, int color, long password) {
		cache.remove(id);
		CompoundTag col = getCol(id, color, password);
		col.putLong("password", password);
		StorageContainer storage = new StorageContainer(id, col);
		cache.put(id, storage);
		return storage;
	}

	private CompoundTag getCol(UUID id, int color, long password) {
		CompoundTag ans;
		if (!storage.containsKey(id.toString())) {
			storage.put(id.toString(), ans = new CompoundTag());
			ans.putUUID("owner_id", id);
		} else ans = storage.get(id);
		CompoundTag col;
		if (ans.contains("color_" + color)) {
			col = ans.getCompound("color_" + color);
		} else {
			col = new CompoundTag();
			col.putLong("password", password);
			ans.put("color_" + color, col);
		}
		return col;
	}

	public void init() {

	}

}
