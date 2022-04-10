package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class CuisineItems {

	public static class Tab extends CreativeModeTab {

		private final Supplier<ItemEntry<?>> icon;

		public Tab(String id, Supplier<ItemEntry<?>> icon) {
			super(LightLand.MODID + "." + id);
			this.icon = icon;
		}

		@Override
		public ItemStack makeIcon() {
			return icon.get().asStack();
		}
	}

	public static final Tab TAB_MAIN = new Tab("cuisine", () -> LightlandItems.MAGIC_WAND);



	public static void register() {

	}

}
