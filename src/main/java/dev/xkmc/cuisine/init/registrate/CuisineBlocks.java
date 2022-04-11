package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.cuisine.content.fruits.CuisineLeaveBlock;
import dev.xkmc.cuisine.content.veges.CuisineCropBlock;
import dev.xkmc.cuisine.init.data.CuisineCropType;
import dev.xkmc.cuisine.init.data.CuisineTreeType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

@SuppressWarnings({"rawtype", "unchecked", "unsafe"})
public class CuisineBlocks {

	static {
		REGISTRATE.creativeModeTab(() -> CuisineItems.TAB_MAIN);
	}

	public static final BlockEntry<CuisineCropBlock>[] VEGES;


	public static final BlockEntry<? extends Block>[] LEAVE, SAPLING;

	static {
		{
			int n = CuisineCropType.values().length;
			VEGES = new BlockEntry[n];
			for (int i = 0; i < n; i++) {
				CuisineCropType type = CuisineCropType.values()[i];
				VEGES[i] = REGISTRATE.block(type.getName(), type::createBlock)
						.addLayer(() -> RenderType::cutout)
						.blockstate(type::generate).item().defaultModel().build()
						.loot(type::loot).defaultLang().register();
			}
		}
		{
			int n = CuisineTreeType.values().length;
			LEAVE = new BlockEntry[n];
			SAPLING = new BlockEntry[n];
			for (int i = 0; i < n; i++) {
				CuisineTreeType type = CuisineTreeType.values()[i];
				LEAVE[i] = REGISTRATE.block("leaves_" + type.getName(), p -> new CuisineLeaveBlock(type,
								BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noCollission()))
						.blockstate(type::generate).loot(type::loot).tag(BlockTags.LEAVES).register();
				SAPLING[i] = REGISTRATE.block("sapling_" + type.getName(), p -> new SaplingBlock(type.getGrower(), p))
						.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(),
								pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
						.tag(BlockTags.SAPLINGS).register();
			}
		}
	}

	public static void register() {

	}

}
