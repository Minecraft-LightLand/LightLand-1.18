package dev.xkmc.cuisine.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import java.util.Locale;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum SimpleItem {
	CHILI_POWDER, CRUDE_SALT, DOUGH, FLOUR, RICE_POWDER, SALT, SICHUAN_PEPPER_POWDER, TOFU, UNREFINED_SUGAR, WHITE_RICE;

	public final ItemEntry<Item> item = REGISTRATE.item(getName(), Item::new).defaultLang().register();

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {
	}


}
