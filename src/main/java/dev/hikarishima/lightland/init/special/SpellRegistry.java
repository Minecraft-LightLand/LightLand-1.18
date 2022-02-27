package dev.hikarishima.lightland.init.special;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.magic.spell.EvokerFangSpell;
import dev.hikarishima.lightland.content.magic.spell.FireArrowSpell;
import dev.hikarishima.lightland.content.magic.spell.WaterTrapSpell;
import dev.hikarishima.lightland.content.magic.spell.WindBladeSpell;
import dev.hikarishima.lightland.content.magic.spell.internal.Spell;
import dev.hikarishima.lightland.init.LightLand;

public class SpellRegistry {

    public static final RegistryEntry<WindBladeSpell> BLADE_SIDE = reg("blade_side", WindBladeSpell::new);
    public static final RegistryEntry<WindBladeSpell> BLADE_FRONT = reg("blade_front", WindBladeSpell::new);
    public static final RegistryEntry<EvokerFangSpell> FANG_ROUND = reg("fang_round", EvokerFangSpell::new);
    public static final RegistryEntry<WaterTrapSpell> WATER_TRAP_0 = reg("water_trap_0", WaterTrapSpell::new);
    public static final RegistryEntry<WaterTrapSpell> WATER_TRAP_1 = reg("water_trap_1", WaterTrapSpell::new);
    public static final RegistryEntry<FireArrowSpell> FIRE_RAIN = reg("fire_rain", FireArrowSpell::new);
    public static final RegistryEntry<FireArrowSpell> EXPLOSION_RAIN = reg("explosion_rain", FireArrowSpell::new);
    public static final RegistryEntry<FireArrowSpell> FIRE_RFIRE_EXPLOSIONAIN = reg("fire_explosion", FireArrowSpell::new);

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Spell<?, ?>> RegistryEntry<T> reg(String id, NonNullSupplier<T> sup) {
        return LightLand.REGISTRATE.generic((Class<Spell<?, ?>>) (Class) Spell.class, id, sup).defaultLang().register();
    }

    public static void register() {
    }

}
