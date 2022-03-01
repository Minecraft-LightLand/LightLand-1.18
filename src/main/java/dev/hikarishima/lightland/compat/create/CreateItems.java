package dev.hikarishima.lightland.compat.create;

import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class CreateItems {

    public static final BlockEntry<Block> STEEL_BLOCK = LightLand.REGISTRATE.block("steel_block",
                    p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
            .defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
    public static final BlockEntry<Block> LEAD_BLOCK = LightLand.REGISTRATE.block("lead_block",
                    p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
            .defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();

    public static final ItemEntry<Item> COAL_IRON_MIX = LightLand.REGISTRATE.item("coal_iron_mix", Item::new)
            .defaultModel().defaultLang().register();
    public static final ItemEntry<Item> HOT_COAL_IRON_MIX = LightLand.REGISTRATE.item("hot_coal_iron_mix", Item::new)
            .defaultModel().defaultLang().register();
    public static final ItemEntry<SequencedAssemblyItem> PROCESSING_STEEL = LightLand.REGISTRATE.item("processing_steel", SequencedAssemblyItem::new)
            .defaultModel().defaultLang().register();
    public static final ItemEntry<Item> STEEL_INGOT = LightLand.REGISTRATE.item("steel_ingot", Item::new)
            .defaultModel().defaultLang().register();
    public static final ItemEntry<Item> STEEL_NUGGET = LightLand.REGISTRATE.item("steel_nugget", Item::new)
            .defaultModel().defaultLang().register();
    public static final ItemEntry<Item> LEAD_INGOT = LightLand.REGISTRATE.item("lead_ingot", Item::new)
            .defaultModel().defaultLang().register();
    public static final ItemEntry<Item> LEAD_NUGGET = LightLand.REGISTRATE.item("lead_nugget", Item::new)
            .defaultModel().defaultLang().register();

    public static void register() {

    }

}
