package dev.xkmc.lightland.network.config;

import dev.xkmc.l2library.network.BaseConfig;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.content.magic.render.SpellComponent;
import dev.xkmc.lightland.network.NetworkManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

@SerialClass
public class SpellEntityConfig extends BaseConfig {

	@SerialClass.SerialField
	public HashMap<String, SpellComponent> map = new HashMap<>();

	@Nullable
	@SuppressWarnings({"unsafe"})
	public static SpellComponent getConfig(ResourceLocation rl) {
		return NetworkManager.getConfigs("config_spell_entity")
				.map(e -> ((SpellEntityConfig) e.getValue()).map.get(rl.toString()))
				.filter(Objects::nonNull).findFirst().orElse(null);

	}

}
