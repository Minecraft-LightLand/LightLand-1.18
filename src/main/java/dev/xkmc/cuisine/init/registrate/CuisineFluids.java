package dev.xkmc.cuisine.init.registrate;

import dev.xkmc.cuisine.init.data.CuisineTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Supplier;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum CuisineFluids {
	EMPTY(0x808080, () -> Fluids.EMPTY),
	WATER(MaterialColor.WATER.col, () -> Fluids.WATER),
	MILK(MaterialColor.SNOW.col, ForgeMod.MILK),
	CUISINE_JUICE(MaterialColor.TERRACOTTA_GRAY.col, null),
	EDIBLE_OIL(MaterialColor.TERRACOTTA_YELLOW.col, null),
	FRUIT_VINEGAR(MaterialColor.TERRACOTTA_BROWN.col, null),
	RICE_VINEGAR(MaterialColor.TERRACOTTA_BLACK.col, null),
	SESAME_OIL(MaterialColor.TERRACOTTA_ORANGE.col, null),
	SOY_MILK(MaterialColor.TERRACOTTA_LIGHT_GRAY.col, null),
	SOY_SAUCE(MaterialColor.COLOR_BROWN.col, null),
	SUGARCANE_JUICE(MaterialColor.GLOW_LICHEN.col, null);

	public final Supplier<Fluid> fluid;
	public final int color;

	CuisineFluids(int color, @Nullable Supplier<Fluid> fluid) {
		if (fluid != null) {
			this.fluid = fluid;
		} else {
			this.fluid = REGISTRATE.virtualFluid(getName()).tag(CuisineTags.AllFluidTags.JAR_ACCEPT.tag).defaultLang().register()::get;
		}
		this.color = color;
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {
	}


}
