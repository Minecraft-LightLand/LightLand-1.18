package dev.hikarishima.lightland.content.common.capability.worldstorage;

import dev.lcy0x1.serial.codec.TagCodec;
import dev.lcy0x1.serial.ExceptionHandler;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SerialClass
public class WorldStorageCapability implements ICapabilitySerializable<CompoundTag> {

	public final ServerLevel w;
	public final WorldStorage handler;
	public final LazyOptional<WorldStorage> lo;

	public WorldStorageCapability(ServerLevel level) {
		this.w = level;
		if (level == null) LogManager.getLogger().error("world not present");
		handler = new WorldStorage(level);
		lo = LazyOptional.of(() -> this.handler);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
		if (capability == WorldStorage.CAPABILITY)
			return lo.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		return TagCodec.toTag(new CompoundTag(), lo.resolve().get());
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		ExceptionHandler.get(() -> TagCodec.fromTag(tag, WorldStorage.class, handler, f -> true));
		handler.init();
	}

}
