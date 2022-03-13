package dev.hikarishima.lightland.compat.create;

import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.world.item.Item;

public class CreateItems {

	public static final ItemEntry<Item> COAL_IRON_MIX = LightLand.REGISTRATE.item("coal_iron_mix", Item::new)
			.defaultModel().defaultLang().register();
	public static final ItemEntry<Item> HOT_COAL_IRON_MIX = LightLand.REGISTRATE.item("hot_coal_iron_mix", Item::new)
			.defaultModel().defaultLang().register();
	public static final ItemEntry<SequencedAssemblyItem> PROCESSING_STEEL = LightLand.REGISTRATE.item("processing_steel", SequencedAssemblyItem::new)
			.defaultModel().defaultLang().register();
	public static final ItemEntry<SequencedAssemblyItem> PROCESSING_LEATHER = LightLand.REGISTRATE.item("processing_leather", SequencedAssemblyItem::new)
			.defaultModel().defaultLang().register();
	public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_ENDER_POCKET = LightLand.REGISTRATE.item("incomplete_ender_pocket", SequencedAssemblyItem::new)
			.defaultModel().defaultLang().register();

	public static void register() {

	}

}
