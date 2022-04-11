package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.lcy0x1.block.DelegateBlock;
import dev.lcy0x1.block.DelegateBlockProperties;
import dev.xkmc.cuisine.content.fruits.CuisineLeaveBlock;
import dev.xkmc.cuisine.content.veges.CuisineCropBlock;
import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.init.data.CuisineCropType;
import dev.xkmc.cuisine.init.data.CuisineTreeType;
import dev.xkmc.cuisine.init.data.WoodType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

@SuppressWarnings({"rawtype", "unchecked", "unsafe"})
public class CuisineBlocks {

	static {
		REGISTRATE.creativeModeTab(() -> CuisineItems.TAB_MAIN);
	}

	public static final BlockEntry<CuisineCropBlock>[] VEGES;


	public static final BlockEntry<CuisineLeaveBlock>[] LEAVE;
	public static final BlockEntry<SaplingBlock>[] SAPLING;

	public static final BlockEntry<Block> BASIN;
	public static final BlockEntry<DelegateBlock> JAR;
	public static final BlockEntry<Block> MORTAR;


	static {
		// crop
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
		// tree
		{
			int n = CuisineTreeType.values().length;
			LEAVE = new BlockEntry[n];
			SAPLING = new BlockEntry[n];
			for (int i = 0; i < n; i++) {
				CuisineTreeType type = CuisineTreeType.values()[i];
				LEAVE[i] = REGISTRATE.block("leaves_" + type.getName(), p -> new CuisineLeaveBlock(type,
								BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).randomTicks().noCollission()))
						.blockstate(type::generate).loot(type::loot).addLayer(() -> RenderType::cutoutMipped)
						.tag(BlockTags.MINEABLE_WITH_HOE, BlockTags.LEAVES).simpleItem().register();
			}
			for (int i = 0; i < n; i++) {
				CuisineTreeType type = CuisineTreeType.values()[i];
				SAPLING[i] = REGISTRATE.block("sapling_" + type.getName(), p -> new SaplingBlock(type.getGrower(),
								BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)))
						.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(),
								pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
						.addLayer(() -> RenderType::cutoutMipped).tag(BlockTags.SAPLINGS)
						.item().model((ctx, pvd) -> pvd.generated(ctx::getEntry,
								pvd.modLoc("block/" + ctx.getName()))).build().register();
			}
			WoodType.register();
		}
		// tools
		{
			DelegateBlockProperties prop = DelegateBlockProperties.copy(Blocks.STONE).make(BlockBehaviour.Properties::noOcclusion);
			JAR = REGISTRATE.block("jar", p -> DelegateBlock.newBaseBlock(prop))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), new ModelFile.UncheckedModelFile(
							new ResourceLocation(Cuisine.MODID, "block/jar"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).simpleItem().register();


			BASIN = REGISTRATE.block("basin", p -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), new ModelFile.UncheckedModelFile(
							new ResourceLocation(Cuisine.MODID, "block/basin"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).simpleItem().register();
			MORTAR = REGISTRATE.block("mortar", p -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()))
					.blockstate((ctx, pvd) -> {
						ModelFile mortar = new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/mortar"));
						ModelFile pestle = new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/pestle"));
						pvd.getMultipartBuilder(ctx.getEntry())
								.part().modelFile(mortar).addModel().end()
								.part().modelFile(pestle).addModel().end();
					}).item().defaultModel().build()
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
		}
	}

	public static void register() {

	}

}
