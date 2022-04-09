package dev.lcy0x1.base;

import dev.lcy0x1.serial.codec.PacketCodec;
import dev.lcy0x1.serial.codec.TagCodec;
import dev.lcy0x1.serial.ExceptionHandler;
import dev.lcy0x1.serial.SerialClass;
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
		tag.put("auto-serial", TagCodec.toTag(new CompoundTag(), this));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (!tag.contains("auto-serial"))
			return;
		ExceptionHandler.run(() -> TagCodec.fromTag(tag.getCompound("auto-serial"), this.getClass(), this, f -> true));
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		PacketCodec.to(buffer, this);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void readSpawnData(FriendlyByteBuf data) {
		PacketCodec.from(data, (Class) this.getClass(), this);
	}

}
