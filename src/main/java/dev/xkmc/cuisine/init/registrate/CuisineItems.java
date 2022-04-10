package dev.xkmc.cuisine.init.registrate;

import dev.hikarishima.lightland.init.LightLand;
import dev.xkmc.cuisine.init.data.CuisineCropType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

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


	public static void register() {

	}

}
