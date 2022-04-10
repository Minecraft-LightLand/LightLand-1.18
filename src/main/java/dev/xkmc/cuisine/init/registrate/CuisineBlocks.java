package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.cuisine.content.veges.CuisineCrops;
import dev.xkmc.cuisine.init.data.CuisineTemplates;
import net.minecraft.client.renderer.RenderType;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public class CuisineBlocks {

	static {
		REGISTRATE.creativeModeTab(() -> CuisineItems.TAB_MAIN);
	}

	public static final BlockEntry<CuisineCrops>[] VEGES;

	static {
		int n = CuisineTemplates.Veges.values().length;
		VEGES = new BlockEntry[n];
		for (int i = 0; i < n; i++) {
			CuisineTemplates.Veges type = CuisineTemplates.Veges.values()[i];
			VEGES[i] = REGISTRATE.block(type.getName(), p-> type.createBlock(p))
					.addLayer(()-> RenderType::cutout)
					.blockstate(type::generate).item().defaultModel().build()
					.loot(type::loot).defaultLang().register();
		}
	}

	public static void register() {

	}

}
