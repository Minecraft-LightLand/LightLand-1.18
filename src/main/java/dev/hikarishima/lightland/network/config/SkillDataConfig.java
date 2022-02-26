package dev.hikarishima.lightland.network.config;

import dev.hikarishima.lightland.content.skill.SkillConfig;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

@SerialClass
public class SkillDataConfig extends ConfigSyncManager.BaseConfig {

    @SerialClass.SerialField(generic = {String.class, SkillConfig.class})
    public HashMap<String, SkillConfig<?>> map = new HashMap<>();

    @Nullable
    @SuppressWarnings({"unchecked", "unsafe"})
    public static <C extends SkillConfig<?>> C getConfig(ResourceLocation rl) {
        return (C) ConfigSyncManager.CONFIGS.entrySet().stream()
                .filter(e -> new ResourceLocation(e.getKey()).getPath().equals("config_skill"))
                .map(e -> ((SkillDataConfig) e.getValue()).map.get(rl.toString()))
                .filter(Objects::nonNull).findFirst().orElse(null);

    }

}
