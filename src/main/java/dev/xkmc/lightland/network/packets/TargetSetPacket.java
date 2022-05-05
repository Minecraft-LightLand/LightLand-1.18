package dev.xkmc.lightland.network.packets;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.network.LLSerialPacket;
import dev.xkmc.lightland.util.RayTraceUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.UUID;

@SerialClass
public class TargetSetPacket extends LLSerialPacket {

	@SerialClass.SerialField
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
	public void handle(NetworkEvent.Context context) {
		RayTraceUtil.sync(this);
	}
}
