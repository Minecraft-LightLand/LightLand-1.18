package dev.hikarishima.lightland.init.special;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.magic.spell.WindBladeSpell;
import dev.hikarishima.lightland.content.magic.spell.internal.Spell;
import dev.hikarishima.lightland.init.LightLand;

public class SpellRegistry {

    public static final RegistryEntry<WindBladeSpell> BLADE_SIDE = reg("blade_side", WindBladeSpell::new);
    public static final RegistryEntry<WindBladeSpell> BLADE_FRONT = reg("blade_front", WindBladeSpell::new);

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Spell<?, ?>> RegistryEntry<T> reg(String id, NonNullSupplier<T> sup) {
        return LightLand.REGISTRATE.generic((Class<Spell<?, ?>>) (Class) Spell.class, id, sup).defaultLang().register();
    }

    public static void register() {
    }

}
