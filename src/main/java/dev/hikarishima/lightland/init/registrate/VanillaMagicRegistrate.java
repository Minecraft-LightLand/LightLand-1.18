package dev.hikarishima.lightland.init.registrate;


import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.common.effect.ArcaneEffect;
import dev.hikarishima.lightland.content.common.effect.FlameEffect;
import dev.hikarishima.lightland.content.common.effect.QuickPullEffect;
import dev.hikarishima.lightland.content.common.effect.WaterTrapEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

/**
 * handles enchantment, mob effects, and potions
 */
public class VanillaMagicRegistrate {

    public static final RegistryEntry<ArcaneEffect> ARCANE = genEffect("arcane", () -> new ArcaneEffect(MobEffectCategory.NEUTRAL, 0x4800FF));
    public static final RegistryEntry<WaterTrapEffect> WATER_TRAP = genEffect("water_trap", () -> new WaterTrapEffect(MobEffectCategory.HARMFUL, 0x7f7fff));
    public static final RegistryEntry<FlameEffect> FLAME = genEffect("flame", () -> new FlameEffect(MobEffectCategory.HARMFUL, 0xFF0000));
    public static final RegistryEntry<QuickPullEffect> QUICK_PULL = genEffect("quick_pull", () -> new QuickPullEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));

    public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup) {
        return REGISTRATE.simple(name, MobEffect.class, sup);
    }

    public static void register() {

    }

}
