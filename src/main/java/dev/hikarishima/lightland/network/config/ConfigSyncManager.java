package dev.hikarishima.lightland.network.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.hikarishima.lightland.network.SerialPacketBase;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.JsonCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConfigSyncManager {

	public static HashMap<String, BaseConfig> CONFIGS = new HashMap<>();

	public static final PreparableReloadListener CONFIG = new SimpleJsonResourceReloadListener(new Gson(), "lightland_config") {
		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller filler) {
			map.forEach((k, v) -> {
				BaseConfig config = JsonCodec.from(v, BaseConfig.class, null);
				if (config != null)
					CONFIGS.put(k.toString(), config);
			});
		}
	};

	@SerialClass
	public static class SyncPacket extends SerialPacketBase {

		@SerialClass.SerialField
		public HashMap<String, BaseConfig> map = null;

		@Deprecated
		public SyncPacket() {

		}

		public SyncPacket(HashMap<String, BaseConfig> map) {
			this.map = map;
		}

		@Override
		public void handle(NetworkEvent.Context ctx) {
			if (map != null)
				CONFIGS = map;
		}

	}

	@SerialClass
	public static class BaseConfig {

	}

	public static void onDatapackSync(OnDatapackSyncEvent event) {
		SyncPacket packet = new SyncPacket(CONFIGS);
		if (event.getPlayer() == null) packet.toAllClient();
		else packet.toClientPlayer(event.getPlayer());
	}

}
