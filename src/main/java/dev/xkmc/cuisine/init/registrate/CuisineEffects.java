package dev.xkmc.cuisine.init.registrate;

import dev.xkmc.l2library.repack.registrate.builders.NoConfigBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.cuisine.content.misc.TasteEffect;
import dev.xkmc.cuisine.content.misc.TasteEffect.Attribs;
import dev.xkmc.cuisine.content.misc.TasteEffect.Config;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Locale;
import java.util.function.Supplier;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum CuisineEffects {
	TOO_SPICY(new Config(MobEffectCategory.HARMFUL, 0xffffff, 20, 1,
			new Attribs(Attributes.ATTACK_DAMAGE, Operation.MULTIPLY_TOTAL, 0.1f))),
	SPICY(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0,
			new Attribs(Attributes.ATTACK_DAMAGE, Operation.MULTIPLY_TOTAL, 0.1f))),
	SWEET(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0,
			new Attribs(Attributes.MOVEMENT_SPEED, Operation.MULTIPLY_TOTAL, 0.2f))),
	TOO_SWEET(() -> new MobEffectInstance(MobEffects.CONFUSION, 600)),
	BLAND(() -> new MobEffectInstance(MobEffects.HUNGER, 300)),
	TASTEFUL(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 20, -1)),

	TOO_SALTY(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	SOUR(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	TOO_SOUR(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	NO_OIL(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	OIL(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	GREASY(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	SESAME(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	KELP(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	NUMB(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	STOCHASTIC(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	FINE(new Config(MobEffectCategory.BENEFICIAL, 0xffffff, 0, 0)),
	;

	private final Supplier<MobEffectInstance> effect;

	CuisineEffects(Config config) {
		RegistryEntry<TasteEffect> entry = genEffect(getName(), () -> new TasteEffect(config, getName()));
		effect = () -> new MobEffectInstance(entry.get(), 1200);
	}

	CuisineEffects(Supplier<MobEffectInstance> effect) {
		this.effect = effect;
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup) {
		return REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(REGISTRATE, REGISTRATE, name, cb, MobEffect.class, sup))
				.lang(MobEffect::getDescriptionId).register();
	}

	public MobEffectInstance getEffect() {
		return effect.get();
	}
}
