package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum PlateItem {
	FISH0, MEAT0, MEAT1, MIXED0, MIXED1, PLATE, RICE0, VEGES0, VEGES1;

	public final ItemEntry<Item> entry;


	PlateItem() {
		entry = REGISTRATE.item(getName(), Item::new)
				.model((ctx, pvd) -> pvd.getBuilder(getName()).parent(new ModelFile.UncheckedModelFile(
						new ResourceLocation(Cuisine.MODID, "block/" + getName())
				))).defaultLang().register();
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register(){

	}

}
