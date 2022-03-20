package dev.hikarishima.lightland.network.config;

import dev.hikarishima.lightland.content.magic.render.SpellComponent;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

@SerialClass
public class SpellEntityConfig extends ConfigSyncManager.BaseConfig {

	@SerialClass.SerialField
	public HashMap<String, SpellComponent> map = new HashMap<>();

	@Nullable
	@SuppressWarnings({"unchecked", "unsafe"})
	public static SpellComponent getConfig(ResourceLocation rl) {
		return ConfigSyncManager.CONFIGS.entrySet().stream()
				.filter(e -> new ResourceLocation(e.getKey()).getPath().equals("config_spell_entity"))
				.map(e -> ((SpellEntityConfig) e.getValue()).map.get(rl.toString()))
				.filter(Objects::nonNull).findFirst().orElse(null);

	}

}
