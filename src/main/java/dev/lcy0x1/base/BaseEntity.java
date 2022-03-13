package dev.lcy0x1.base;

import dev.lcy0x1.util.Automator;
import dev.lcy0x1.util.ExceptionHandler;
import dev.lcy0x1.util.SerialClass;
import dev.lcy0x1.util.Serializer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.ParametersAreNonnullByDefault;

@SerialClass
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseEntity extends Entity implements IEntityAdditionalSpawnData {

	public BaseEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.put("auto-serial", Automator.toTag(new CompoundTag(), this));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (!tag.contains("auto-serial"))
			return;
		ExceptionHandler.run(() -> Automator.fromTag(tag.getCompound("auto-serial"), this.getClass(), this, f -> true));
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		Serializer.to(buffer, this);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void readSpawnData(FriendlyByteBuf data) {
		Serializer.from(data, (Class) this.getClass(), this);
	}

}
