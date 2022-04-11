package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.init.LightLand;
import dev.xkmc.cuisine.init.data.CuisineCropType;
import dev.xkmc.cuisine.init.data.CuisineTreeType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public class CuisineItems {

	public static class Tab extends CreativeModeTab {

		private final Supplier<Supplier<Item>> icon;

		public Tab(String id, Supplier<Supplier<Item>> icon) {
			super(LightLand.MODID + "." + id);
			this.icon = icon;
		}

		@Override
		public ItemStack makeIcon() {
			return icon.get().get().getDefaultInstance();
		}
	}

	public static final Tab TAB_MAIN = new Tab("cuisine", () -> CuisineCropType.CHILI::getSeed);

	public static final ItemEntry<?>[] FRUITS;

	static {
		int n = CuisineTreeType.values().length;
		FRUITS = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			CuisineTreeType type = CuisineTreeType.values()[i];
			FRUITS[i] = REGISTRATE.item(type.getName(), Item::new).register();
		}
	}

	public static void register() {

	}

}
