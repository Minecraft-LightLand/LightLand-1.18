package dev.xkmc.lightland.network.config;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.content.magic.products.info.TypeConfig;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

@SerialClass
public class ProductTypeConfig extends ConfigSyncManager.BaseConfig {

	@SerialClass.SerialField
	public HashMap<String, TypeConfig> map = new HashMap<>();

	@Nullable
	@SuppressWarnings({"unchecked", "unsafe"})
	public static TypeConfig getConfig(ResourceLocation rl) {
		return ConfigSyncManager.CONFIGS.entrySet().stream()
				.filter(e -> new ResourceLocation(e.getKey()).getPath().equals("config_product_type"))
				.map(e -> ((ProductTypeConfig) e.getValue()).map.get(rl.toString()))
				.filter(Objects::nonNull).findFirst().orElse(null);

	}

}
