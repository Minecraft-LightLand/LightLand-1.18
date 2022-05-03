package dev.xkmc.lightland.network.packets;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.lightland.content.common.capability.player.CapProxy;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.common.capability.player.SkillCap;
import dev.xkmc.lightland.network.SerialPacketBase;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SkillToServer extends SerialPacketBase {

	@SerialClass.SerialField
	public int slot;

	@Deprecated
	public SkillToServer() {

	}

	public SkillToServer(int slot) {
		this.slot = slot;
	}

	@OnlyIn(Dist.CLIENT)
	public static void clientActivate(int slot) {
		AbstractClientPlayer player = Proxy.getClientPlayer();
		LLPlayerData data = CapProxy.getHandler();
		if (slot >= data.skillCap.list.size()) return;
		SkillCap.Cont<?, ?, ?> cont = data.skillCap.list.get(slot);
		if (!cont.canActivate(player.level, player)) return;
		new SkillToServer(slot).toServer();
	}

	@Override
	public void handle(NetworkEvent.Context ctx) {
		ServerPlayer player = ctx.getSender();
		if (player == null) return;
		LLPlayerData data = LLPlayerData.get(player);
		if (slot >= data.skillCap.list.size()) return;
		SkillCap.Cont<?, ?, ?> cont = data.skillCap.list.get(slot);
		if (!cont.canActivate(player.level, player)) return;
		cont.activate(player.level, player);
		new CapToClient(CapToClient.Action.SKILL, data).toClientPlayer(player);
	}

}
