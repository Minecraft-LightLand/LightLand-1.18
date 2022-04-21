package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.cuisine.content.misc.TasteEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Locale;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum CuisineEffects {
	TOO_SPICY, SPICY, SWEET, TOO_SWEET,
	BLAND, TASTEFUL, TOO_SALTY,
	SOUR, TOO_SOUR, NO_OIL, OIL, GREASY,
	SESAME, KELP, NUMB;

	public final RegistryEntry<TasteEffect> effect;


	CuisineEffects() {
		effect = genEffect(getName(), () -> new TasteEffect(MobEffectCategory.NEUTRAL, 0x4800FF));
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup) {
		return REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(REGISTRATE, REGISTRATE, name, cb, MobEffect.class, sup))
				.lang(MobEffect::getDescriptionId).register();
	}

	public MobEffectInstance getEffect() {
		return new MobEffectInstance(effect.get(), 1200);
	}
}
