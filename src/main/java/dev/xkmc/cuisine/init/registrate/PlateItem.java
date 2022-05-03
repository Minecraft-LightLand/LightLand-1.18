package dev.xkmc.cuisine.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public enum PlateItem {
	FISH0, MEAT0, MEAT1, MIXED0, MIXED1, PLATE, RICE0, VEGES0, VEGES1;

	public final BlockEntry<Block> entry;


	PlateItem() {
		entry = REGISTRATE.block(getName(), p -> new Block(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)))
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), new ModelFile.UncheckedModelFile(
						new ResourceLocation(Cuisine.MODID, "block/" + getName())))).simpleItem().defaultLang().register();
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {

	}

}
