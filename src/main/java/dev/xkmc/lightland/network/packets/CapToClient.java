package dev.xkmc.lightland.network.packets;

import dev.xkmc.l2library.serial.ExceptionHandler;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.lightland.content.common.capability.player.*;
import dev.xkmc.lightland.network.SerialPacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Consumer;
import java.util.function.Function;

@SerialClass
public class CapToClient extends SerialPacketBase {

	@SerialClass.SerialField
	public Action action;

	@SerialClass.SerialField
	public CompoundTag tag;

	@Deprecated
	public CapToClient() {

	}

	public CapToClient(Action action, LLPlayerData handler) {
		this.action = action;
		this.tag = action.server.apply(handler);
	}

	public void handle(NetworkEvent.Context context) {
		if (action != Action.ALL && action != Action.CLONE && !Proxy.getClientPlayer().isAlive())
			return;
		action.client.accept(tag);
	}

	public static void reset(ServerPlayer e, LLPlayerData.Reset reset) {
		CapToClient msg = new CapToClient(Action.RESET, null);
		msg.tag.putInt("ordinal", reset.ordinal());
		msg.toClientPlayer(e);
	}

	public enum Action {
		DEBUG((m) -> TagCodec.toTag(new CompoundTag(), m), (tag) -> {
			LLPlayerData m = CapProxy.getHandler();
			CompoundTag comp = ExceptionHandler.get(() -> TagCodec.toTag(new CompoundTag(), LLPlayerData.class, m, f -> true));
			CapToServer.sendDebugInfo(tag, comp);
		}),
		ALL((m) -> {
			m.magicAbility.time_after_sync = 0;
			return TagCodec.toTag(new CompoundTag(), m);
		}, tag -> LLPlayerData.cacheSet(tag, false)),
		CLONE((m) -> {
			m.magicAbility.time_after_sync = 0;
			return TagCodec.toTag(new CompoundTag(), m);
		}, tag -> LLPlayerData.cacheSet(tag, true)),
		ARCANE_TYPE((m) -> m.magicAbility.arcane_type, (tag) -> {
			MagicAbility abi = CapProxy.getHandler().magicAbility;
			abi.arcane_type = tag;
		}), MAGIC_ABILITY((m) -> {
			m.magicAbility.time_after_sync = 0;
			return TagCodec.toTag(new CompoundTag(), m.magicAbility);
		}, (tag) -> {
			LLPlayerData h = CapProxy.getHandler();
			h.magicAbility = new MagicAbility(h);
			ExceptionHandler.run(() -> TagCodec.fromTag(tag, MagicAbility.class, h.magicAbility, f -> true));
			h.reInit();
		}), ABILITY_POINT((m) -> TagCodec.toTag(new CompoundTag(), m.abilityPoints), (tag) -> {
			LLPlayerData h = CapProxy.getHandler();
			h.abilityPoints = new AbilityPoints(h);
			ExceptionHandler.run(() -> TagCodec.fromTag(tag, AbilityPoints.class, h.abilityPoints, f -> true));
			h.reInit();
		}), RESET(m -> new CompoundTag(), tag -> {
			LLPlayerData h = CapProxy.getHandler();
			h.reset(LLPlayerData.Reset.values()[tag.getInt("ordinal")]);
		}), SKILL(m -> TagCodec.toTag(new CompoundTag(), m.skillCap), tag -> {
			LLPlayerData h = CapProxy.getHandler();
			h.skillCap = new SkillCap(h);
			ExceptionHandler.run(() -> TagCodec.fromTag(tag, SkillCap.class, h.skillCap, f -> true));
		});

		public final Function<LLPlayerData, CompoundTag> server;
		public final Consumer<CompoundTag> client;


		Action(Function<LLPlayerData, CompoundTag> server, Consumer<CompoundTag> client) {
			this.server = server;
			this.client = client;
		}
	}

}
