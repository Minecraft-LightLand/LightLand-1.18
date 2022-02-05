package dev.hikarishima.lightland.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public abstract class SimplePacketBase {

	public abstract void write(FriendlyByteBuf buffer);

	public abstract void handle(Supplier<Context> context);

	public void toServer(){
		PacketHandler.channel.sendToServer(this);
	}

}
