package dev.hikarishima.lightland.network;

import dev.lcy0x1.util.Automator;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@SerialClass
public abstract class SerialPacketBase extends SimplePacketBase {

    public static <T extends SerialPacketBase> T serial(Class<T> cls, FriendlyByteBuf buf) {
        CompoundTag tag = buf.readAnySizeNbt();
        return Automator.fromTag(tag, cls);
    }

    @Override
    public final void write(FriendlyByteBuf buffer) {
        CompoundTag tag = Automator.toTag(new CompoundTag(), this);
        buffer.writeNbt(tag);
    }

    @Override
    public final void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handle(context.get()));
        context.get().setPacketHandled(true);
    }

    public abstract void handle(NetworkEvent.Context ctx);

}
