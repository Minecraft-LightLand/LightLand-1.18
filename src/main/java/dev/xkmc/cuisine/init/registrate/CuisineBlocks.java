package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.cuisine.content.veges.CuisineCropBlock;
import dev.xkmc.cuisine.init.data.CuisineCropType;
import net.minecraft.client.renderer.RenderType;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public class CuisineBlocks {

	static {
		REGISTRATE.creativeModeTab(() -> CuisineItems.TAB_MAIN);
	}

	public static final BlockEntry<CuisineCropBlock>[] VEGES;

	static {
		int n = CuisineCropType.values().length;
		VEGES = new BlockEntry[n];
		for (int i = 0; i < n; i++) {
			CuisineCropType type = CuisineCropType.values()[i];
			VEGES[i] = REGISTRATE.block(type.getName(), p-> type.createBlock(p))
					.addLayer(()-> RenderType::cutout)
					.blockstate(type::generate).item().defaultModel().build()
					.loot(type::loot).defaultLang().register();
		}
	}

	public static void register() {

	}

}
