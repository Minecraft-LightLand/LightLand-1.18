package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.FluidEntry;
import dev.hikarishima.lightland.init.special.VirtualFluid;

import java.util.Locale;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum CuisineFluids {
	CUISINE_JUICE, EDIBLE_OIL, FRUIT_VINEGAR, SESAME_OIL, SOY_MILK, SOY_SAUCE, SUGARCANE_JUICE;

	public final FluidEntry<VirtualFluid> fluid = REGISTRATE.virtualFluid(getName()).defaultLang().register();

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {
	}

}
