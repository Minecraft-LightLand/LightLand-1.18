package dev.hikarishima.lightland.events.generic;

import dev.hikarishima.lightland.content.common.capability.player.LLPlayerCapability;
import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.common.capability.worldstorage.WorldStorageCapability;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.network.packets.CapToClient;
import dev.lcy0x1.util.Automator;
import dev.lcy0x1.util.ExceptionHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvents {

	@SubscribeEvent
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player player) {
			event.addCapability(new ResourceLocation(LightLand.MODID, "player_data"),
					new LLPlayerCapability(player, player.level));
		}
	}


	@SubscribeEvent
	public static void onAttachLevelCapabilities(AttachCapabilitiesEvent<Level> event) {
		if (event.getObject() instanceof ServerLevel level) {
			if (level.dimension() == Level.OVERWORLD) {
				event.addCapability(new ResourceLocation(LightLand.MODID, "world_storage"),
						new WorldStorageCapability(level));
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player.isAlive())
			LLPlayerData.get(event.player).tick();
	}

	@SubscribeEvent
	public static void onServerPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayer e = (ServerPlayer) event.getPlayer();
		if (e != null) {
			new CapToClient(CapToClient.Action.ALL, LLPlayerData.get(e)).toClientPlayer(e);
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		CompoundTag tag0 = Automator.toTag(new CompoundTag(), LLPlayerData.get(event.getOriginal()));
		ExceptionHandler.run(() -> Automator.fromTag(tag0, LLPlayerData.class, LLPlayerData.get(event.getPlayer()), f -> true));
		LLPlayerData.get(event.getPlayer());
		ServerPlayer e = (ServerPlayer) event.getPlayer();
		new CapToClient(CapToClient.Action.CLONE, LLPlayerData.get(e)).toClientPlayer(e);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onPlayerRespawn(ClientPlayerNetworkEvent.RespawnEvent event) {
		CompoundTag tag0 = LLPlayerData.getCache(event.getOldPlayer());
		ExceptionHandler.run(() -> Automator.fromTag(tag0, LLPlayerData.class, LLPlayerData.get(event.getNewPlayer()), f -> true));
		LLPlayerData.get(event.getNewPlayer());
	}

}
