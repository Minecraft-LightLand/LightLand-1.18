package dev.xkmc.lightland.network.config;

import dev.xkmc.l2library.network.BaseConfig;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.content.magic.products.info.TypeConfig;
import dev.xkmc.lightland.network.NetworkManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

@SerialClass
public class ProductTypeConfig extends BaseConfig {

	@SerialClass.SerialField
	public HashMap<String, TypeConfig> map = new HashMap<>();

	@Nullable
	@SuppressWarnings({"unsafe"})
	public static TypeConfig getConfig(ResourceLocation rl) {
		return NetworkManager.getConfigs("config_product_type")
				.map(e -> ((ProductTypeConfig) e.getValue()).map.get(rl.toString()))
				.filter(Objects::nonNull).findFirst().orElse(null);
	}

}
