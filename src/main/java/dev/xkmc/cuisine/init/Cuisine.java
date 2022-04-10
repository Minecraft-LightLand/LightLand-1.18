package dev.xkmc.cuisine.init;

import com.tterrag.registrate.AbstractRegistrate;
import dev.hikarishima.lightland.init.LightLand;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineItems;

public class Cuisine {

	public static final String MODID = LightLand.MODID;
	public static final AbstractRegistrate<?> REGISTRATE = LightLand.REGISTRATE;

	public Cuisine() {
		CuisineBlocks.register();
		CuisineItems.register();
	}

}
