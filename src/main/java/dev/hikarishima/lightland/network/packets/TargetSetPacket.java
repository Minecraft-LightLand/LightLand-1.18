package dev.hikarishima.lightland.network.packets;

import dev.hikarishima.lightland.network.SimplePacketBase;
import dev.hikarishima.lightland.util.RayTraceUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

public class TargetSetPacket extends SimplePacketBase {

	public UUID player, target;

	public TargetSetPacket(UUID player, @Nullable UUID target) {
		this.player = player;
		this.target = target;
	}

	public TargetSetPacket(FriendlyByteBuf buf) {
		player = buf.readUUID();
		boolean exist = buf.readBoolean();
		if (exist) {
			target = buf.readUUID();
		}
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUUID(player);
		buffer.writeBoolean(target != null);
		if (target != null) {
			buffer.writeUUID(target);
		}
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> RayTraceUtil.sync(this));
		context.get().setPacketHandled(true);
	}
}
