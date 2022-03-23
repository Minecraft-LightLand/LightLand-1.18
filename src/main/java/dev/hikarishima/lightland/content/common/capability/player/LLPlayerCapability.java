package dev.hikarishima.lightland.content.common.capability.player;

import dev.lcy0x1.util.Automator;
import dev.lcy0x1.util.ExceptionHandler;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SerialClass
public class LLPlayerCapability implements ICapabilitySerializable<CompoundTag> {

	public final Player player;
	public final Level w;
	public LLPlayerData handler = new LLPlayerData();
	public LazyOptional<LLPlayerData> lo = LazyOptional.of(() -> this.handler);

	public LLPlayerCapability(Player player, Level w) {
		this.player = player;
		this.w = w;
		if (w == null)
			LogManager.getLogger().error("world not present in entity");
		handler.world = w;
		handler.player = player;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
		if (capability == LLPlayerData.CAPABILITY)
			return lo.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		return Automator.toTag(new CompoundTag(), lo.resolve().get());
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		ExceptionHandler.get(() -> Automator.fromTag(tag, LLPlayerData.class, handler, f -> true));
		handler.init();
	}

}
