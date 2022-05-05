package dev.xkmc.lightland.network.config;

import dev.xkmc.l2library.network.BaseConfig;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.content.skill.internal.SkillConfig;
import dev.xkmc.lightland.network.NetworkManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

@SerialClass
public class SkillDataConfig extends BaseConfig {

	@SerialClass.SerialField
	public HashMap<String, SkillConfig<?>> map = new HashMap<>();

	@Nullable
	@SuppressWarnings({"unchecked", "unsafe"})
	public static <C extends SkillConfig<?>> C getConfig(ResourceLocation rl) {
		return (C) NetworkManager.getConfigs("config_skill")
				.map(e -> ((SkillDataConfig) e.getValue()).map.get(rl.toString()))
				.filter(Objects::nonNull).findFirst().orElse(null);

	}

}
