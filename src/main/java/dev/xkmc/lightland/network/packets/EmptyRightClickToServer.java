package dev.xkmc.lightland.network.packets;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.events.ItemUseEventHandler;
import dev.xkmc.lightland.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class EmptyRightClickToServer extends SerialPacketBase {

	@SerialClass.SerialField
	public boolean hand, right;

	public EmptyRightClickToServer(boolean right, boolean hand) {
		this.right = right;
		this.hand = hand;
	}

	@Deprecated
	public EmptyRightClickToServer() {
		this(true, true);
	}

	public void handle(NetworkEvent.Context ctx) {
		ServerPlayer pl = ctx.getSender();
		if (pl != null) {
			if (right) {
				PlayerInteractEvent.RightClickEmpty event = new PlayerInteractEvent.RightClickEmpty(pl,
						hand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
				ItemUseEventHandler.onPlayerRightClickEmpty(event);
			} else {
				PlayerInteractEvent.LeftClickEmpty event = new PlayerInteractEvent.LeftClickEmpty(pl);
				ItemUseEventHandler.onPlayerLeftClickEmpty(event);
			}
		}
	}


}
