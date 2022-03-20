package dev.hikarishima.lightland.network.config;

import dev.hikarishima.lightland.content.magic.spell.internal.SpellConfig;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

@SerialClass
public class SpellDataConfig extends ConfigSyncManager.BaseConfig {

	@SerialClass.SerialField
	public HashMap<String, SpellConfig> map = new HashMap<>();

	@Nullable
	@SuppressWarnings({"unchecked", "unsafe"})
	public static <C extends SpellConfig> C getConfig(ResourceLocation rl) {
		return (C) ConfigSyncManager.CONFIGS.entrySet().stream()
				.filter(e -> new ResourceLocation(e.getKey()).getPath().equals("config_spell"))
				.map(e -> ((SpellDataConfig) e.getValue()).map.get(rl.toString()))
				.filter(Objects::nonNull).findFirst().orElse(null);

	}

}
