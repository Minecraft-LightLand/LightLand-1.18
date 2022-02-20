package dev.hikarishima.lightland.init.special;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.skill.Skill;
import dev.hikarishima.lightland.init.LightLand;

public class SkillRegistry {



    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Skill<?, ?>> RegistryEntry<T> reg(String id, NonNullSupplier<T> sup) {
        return LightLand.REGISTRATE.generic((Class<Skill<?, ?>>) (Class) Skill.class, id, sup).defaultLang().register();
    }

    public static void register() {
    }

}
