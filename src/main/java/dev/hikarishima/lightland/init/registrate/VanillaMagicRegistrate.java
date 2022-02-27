package dev.hikarishima.lightland.init.registrate;


import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.berserker.effect.BloodThurstEffect;
import dev.hikarishima.lightland.content.berserker.effect.NoKnockBackEffect;
import dev.hikarishima.lightland.content.common.effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

/**
 * handles enchantment, mob effects, and potions
 */
public class VanillaMagicRegistrate {

    public static final RegistryEntry<ArcaneEffect> ARCANE = genEffect("arcane", () -> new ArcaneEffect(MobEffectCategory.NEUTRAL, 0x4800FF));
    public static final RegistryEntry<WaterTrapEffect> WATER_TRAP = genEffect("water_trap", () -> new WaterTrapEffect(MobEffectCategory.HARMFUL, 0x00007f));
    public static final RegistryEntry<HeavyEffect> HEAVY = genEffect("heavy", () -> new HeavyEffect(MobEffectCategory.HARMFUL, 0x404040));
    public static final RegistryEntry<FlameEffect> FLAME = genEffect("flame", () -> new FlameEffect(MobEffectCategory.HARMFUL, 0xFF0000));
    public static final RegistryEntry<QuickPullEffect> QUICK_PULL = genEffect("quick_pull", () -> new QuickPullEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
    public static final RegistryEntry<EmeraldPopeEffect> EMERALD = genEffect("emerald", () -> new EmeraldPopeEffect(MobEffectCategory.NEUTRAL, 0x00FF00));
    public static final RegistryEntry<IceEffect> ICE = genEffect("frozen", () -> new IceEffect(MobEffectCategory.HARMFUL, 0x7f7fff));
    public static final RegistryEntry<NoKnockBackEffect> NO_KB = genEffect("no_knockback", () -> new NoKnockBackEffect(MobEffectCategory.BENEFICIAL, 0xafafaf));
    public static final RegistryEntry<BloodThurstEffect> BLOOD_THURST = genEffect("blood_thirst", () -> new BloodThurstEffect(MobEffectCategory.BENEFICIAL, 0xffafaf));
    public static final RegistryEntry<MobEffect> RUN_BOW = genEffect("run_bow",()->new RunBowEffect(MobEffectCategory.BENEFICIAL, 0xffffff));

    public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup) {
        return REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(REGISTRATE, REGISTRATE, name, cb, MobEffect.class, sup))
                .lang(MobEffect::getDescriptionId).register();
    }

    public static void register() {

    }

}
