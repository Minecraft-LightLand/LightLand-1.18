package dev.xkmc.lightland.init.special;

import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.lightland.content.arcane.internal.Arcane;
import dev.xkmc.lightland.content.arcane.magic.*;
import dev.xkmc.lightland.init.LightLand;

@SuppressWarnings("unused")
public class ArcaneRegistry {

	public static final int ARCANE_TIME = 600;

	public static final RegistryEntry<DamageAxe> DUBHE_DAMAGE = reg("damage_axe", () -> new DamageAxe(50, 30));

	public static final RegistryEntry<ThunderAxe> MERAK_THUNDER = reg("thunder_axe", () -> new ThunderAxe(20, 64f));

	public static final RegistryEntry<EarthAxe> PHECDA_EARTH_AXE = reg("earth_axe", () -> new EarthAxe(3f, 60));

	public static final RegistryEntry<WindBladeSword> ALIOTH_WINDBLADE = reg("wind_blade", () -> new WindBladeSword(5f, 1f, 64f));
	public static final RegistryEntry<FireSword> ALIOTH_FIRE = reg("soul_fire_sword", () -> new FireSword(3f, 100));
	public static final RegistryEntry<DamageSword> ALIOTH_DAMAGE = reg("damage_sword", () -> new DamageSword(6f, 5));

	public static final RegistryEntry<WaterSword> MIZAR_WATER = reg("water_trap_sword", () -> new WaterSword(5f, 60));

	public static final RegistryEntry<ThunderSword> ALKAID_THUNDER = reg("thunder_sword", () -> new ThunderSword(20, 64f));
	public static final RegistryEntry<MarkerSword> ALKAID_MARKER = reg("marker", () -> new MarkerSword(30, 32));

	private static <T extends Arcane> RegistryEntry<T> reg(String str, NonNullSupplier<T> a) {
		return LightLand.REGISTRATE.generic(Arcane.class, str, a).defaultLang().register();
	}

	public static void register() {
	}

}
