package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import dev.xkmc.cuisine.content.misc.CuisineBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Supplier;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum CuisineFluids {
	EMPTY(0x808080, () -> Fluids.EMPTY),
	WATER(MaterialColor.WATER.col, () -> Fluids.WATER),
	MILK(MaterialColor.SNOW.col, ForgeMod.MILK),
	EDIBLE_OIL(MaterialColor.TERRACOTTA_YELLOW.col, null),
	FRUIT_VINEGAR(MaterialColor.TERRACOTTA_BROWN.col, null),
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
			this.fluid = REGISTRATE.virtualFluid(getName()).defaultLang().register()::get;
		}
		this.color = color;
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {
	}


}
