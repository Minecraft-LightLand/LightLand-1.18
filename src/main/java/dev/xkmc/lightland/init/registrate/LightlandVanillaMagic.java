package dev.xkmc.lightland.init.registrate;


import dev.xkmc.l2library.repack.registrate.builders.NoConfigBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.lightland.content.assassin.effect.TargetAttractedEffect;
import dev.xkmc.lightland.content.assassin.effect.TargetAttractorEffect;
import dev.xkmc.lightland.content.assassin.effect.TargetHideEffect;
import dev.xkmc.lightland.content.assassin.effect.TargetRemoveEffect;
import dev.xkmc.lightland.content.common.effect.ArmorReduceEffect;
import dev.xkmc.lightland.content.common.effect.CleanseEffect;
import dev.xkmc.lightland.content.common.effect.DispellEffect;
import dev.xkmc.lightland.content.common.effect.EmeraldPopeEffect;
import dev.xkmc.lightland.content.common.effect.force.*;
import dev.xkmc.lightland.content.common.effect.skill.*;
import dev.xkmc.lightland.init.LightLand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;

/**
 * handles enchantment, mob effects, and potions
 */
public class LightlandVanillaMagic {

	public static final RegistryEntry<ArcaneEffect> ARCANE = genEffect("arcane", () -> new ArcaneEffect(MobEffectCategory.NEUTRAL, 0x4800FF));
	public static final RegistryEntry<WaterTrapEffect> WATER_TRAP = genEffect("water_trap", () -> new WaterTrapEffect(MobEffectCategory.HARMFUL, 0x00007f));
	public static final RegistryEntry<HeavyEffect> HEAVY = genEffect("heavy", () -> new HeavyEffect(MobEffectCategory.HARMFUL, 0x404040));
	public static final RegistryEntry<FlameEffect> FLAME = genEffect("flame", () -> new FlameEffect(MobEffectCategory.HARMFUL, 0xFF0000));

	public static final RegistryEntry<EmeraldPopeEffect> EMERALD = genEffect("emerald", () -> new EmeraldPopeEffect(MobEffectCategory.NEUTRAL, 0x00FF00));
	public static final RegistryEntry<IceEffect> ICE = genEffect("frozen", () -> new IceEffect(MobEffectCategory.HARMFUL, 0x7f7fff));
	public static final RegistryEntry<ArmorReduceEffect> ARMOR_REDUCE = genEffect("armor_reduce", () -> new ArmorReduceEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));

	public static final RegistryEntry<NoKnockBackEffect> NO_KB = genEffect("no_knockback", () -> new NoKnockBackEffect(MobEffectCategory.BENEFICIAL, 0xafafaf));
	public static final RegistryEntry<BloodThurstEffect> BLOOD_THURST = genEffect("blood_thirst", () -> new BloodThurstEffect(MobEffectCategory.BENEFICIAL, 0xffafaf));
	public static final RegistryEntry<ArmorBreakerEffect> ARMOR_BREAKER = genEffect("armor_breaker", () -> new ArmorBreakerEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));

	public static final RegistryEntry<MobEffect> RUN_BOW = genEffect("run_bow", () -> new RunBowEffect(MobEffectCategory.BENEFICIAL, 0xffffff));
	public static final RegistryEntry<QuickPullEffect> QUICK_PULL = genEffect("quick_pull", () -> new QuickPullEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));

	public static final RegistryEntry<TargetAttractorEffect> T_SINK = genEffect("target_attractor", () -> new TargetAttractorEffect(MobEffectCategory.NEUTRAL, 0xffffff));
	public static final RegistryEntry<TargetAttractedEffect> T_SOURCE = genEffect("target_attracted", () -> new TargetAttractedEffect(MobEffectCategory.NEUTRAL, 0xffffff));
	public static final RegistryEntry<TargetRemoveEffect> T_CLEAR = genEffect("target_remove", () -> new TargetRemoveEffect(MobEffectCategory.NEUTRAL, 0xffffff));
	public static final RegistryEntry<TargetHideEffect> T_HIDE = genEffect("target_hide", () -> new TargetHideEffect(MobEffectCategory.NEUTRAL, 0xffffff));

	public static final RegistryEntry<CleanseEffect> CLEANSE = genEffect("cleanse", () -> new CleanseEffect(MobEffectCategory.NEUTRAL, 0xffffff));
	public static final RegistryEntry<DispellEffect> DISPELL = genEffect("dispell", () -> new DispellEffect(MobEffectCategory.NEUTRAL, 0x9f9f9f));


	public static final List<RegistryEntry<? extends Potion>> POTION_LIST = new ArrayList<>();

	public static final RegistryEntry<Potion> P_CLEANSE_WATER = genPotion("cleanse_water", () -> new Potion(new MobEffectInstance(CLEANSE.get(), 600)));
	public static final RegistryEntry<Potion> P_CLEANSE_WATER_L = genPotion("long_cleanse_water", () -> new Potion(new MobEffectInstance(CLEANSE.get(), 1200)));
	public static final RegistryEntry<Potion> P_HOLY_WATER = genPotion("holy_water", () -> new Potion(new MobEffectInstance(CLEANSE.get(), 600)));
	public static final RegistryEntry<Potion> P_HOLY_WATER_L = genPotion("long_holy_water", () -> new Potion(new MobEffectInstance(CLEANSE.get(), 1200)));
	public static final RegistryEntry<Potion> P_DISPELL = genPotion("dispell", () -> new Potion(new MobEffectInstance(DISPELL.get(), 600)));
	public static final RegistryEntry<Potion> P_DISPELL_S = genPotion("strong_dispell", () -> new Potion(new MobEffectInstance(DISPELL.get(), 400, 1)));
	public static final RegistryEntry<Potion> P_DISPELL_L = genPotion("long_dispell", () -> new Potion(new MobEffectInstance(DISPELL.get(), 1200)));

	public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup) {
		return LightLand.REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(LightLand.REGISTRATE, LightLand.REGISTRATE, name, cb, MobEffect.class, sup))
				.lang(MobEffect::getDescriptionId).register();
	}

	public static <T extends Potion> RegistryEntry<T> genPotion(String name, NonNullSupplier<T> sup) {
		RegistryEntry<T> ans = LightLand.REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(LightLand.REGISTRATE, LightLand.REGISTRATE, name, cb, Potion.class, sup)).register();
		POTION_LIST.add(ans);
		return ans;
	}

	public static void register() {

	}

	public static void registerBrewingRecipe() {
		PotionBrewing.addMix(Potions.AWKWARD, LightlandItems.CLEANSE_WATER_BOTTLE.get(), P_CLEANSE_WATER.get());
		PotionBrewing.addMix(P_CLEANSE_WATER.get(), Items.REDSTONE, P_CLEANSE_WATER_L.get());
		PotionBrewing.addMix(P_CLEANSE_WATER.get(), LightlandItems.DISPELL_DUST.get(), P_DISPELL.get());
		PotionBrewing.addMix(P_CLEANSE_WATER_L.get(), LightlandItems.DISPELL_DUST.get(), P_DISPELL_L.get());
		PotionBrewing.addMix(P_DISPELL.get(), Items.GLOWSTONE_DUST, P_DISPELL_S.get());
		PotionBrewing.addMix(P_DISPELL.get(), Items.REDSTONE, P_DISPELL_L.get());
	}

}
