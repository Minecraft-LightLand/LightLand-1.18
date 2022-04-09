package dev.lcy0x1.serial.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class CodecHandler<T> extends ClassHandler<Tag, T> {

	public CodecHandler(Class<T> cls, Codec<T> codec, Function<FriendlyByteBuf, T> fp, BiConsumer<FriendlyByteBuf, T> tp) {
		super(cls, e -> codec.decode(JsonOps.INSTANCE, e).result().get().getFirst(), fp, tp,
				e -> codec.decode(NbtOps.INSTANCE, e).result().get().getFirst(),
				e -> codec.encodeStart(NbtOps.INSTANCE, e).result().get());
	}

}
