package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.lcy0x1.block.DelegateBlock;
import dev.lcy0x1.block.DelegateBlockProperties;
import dev.xkmc.cuisine.content.fruits.CuisineLeaveBlock;
import dev.xkmc.cuisine.content.tools.basin.BasinBlock;
import dev.xkmc.cuisine.content.tools.basin.BasinBlockEntity;
import dev.xkmc.cuisine.content.tools.basin.BasinRenderer;
import dev.xkmc.cuisine.content.tools.jar.JarBlock;
import dev.xkmc.cuisine.content.tools.jar.JarBlockEntity;
import dev.xkmc.cuisine.content.tools.mill.MillBlock;
import dev.xkmc.cuisine.content.tools.mill.MillBlockEntity;
import dev.xkmc.cuisine.content.tools.mill.MillRenderer;
import dev.xkmc.cuisine.content.tools.mortar.MortarBlock;
import dev.xkmc.cuisine.content.tools.mortar.MortarBlockEntity;
import dev.xkmc.cuisine.content.tools.mortar.MortarRenderer;
import dev.xkmc.cuisine.content.tools.pan.PanBlock;
import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import dev.xkmc.cuisine.content.tools.pan.PanRenderer;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

@SuppressWarnings({"rawtype", "unchecked", "unsafe"})
public class CuisineBlocks {

	static {
		REGISTRATE.creativeModeTab(() -> CuisineItems.TAB_MAIN);
	}

	public static final BlockEntry<CuisineLeaveBlock>[] LEAVE;
	public static final BlockEntry<SaplingBlock>[] SAPLING;

	public static final BlockEntry<DelegateBlock> PAN, JAR, BASIN, MILL, MORTAR;

	public static final BlockEntityEntry<PanBlockEntity> TE_PAN;
	public static final BlockEntityEntry<JarBlockEntity> TE_JAR;
	public static final BlockEntityEntry<BasinBlockEntity> TE_BASIN;
	public static final BlockEntityEntry<MillBlockEntity> TE_MILL;
	public static final BlockEntityEntry<MortarBlockEntity> TE_MORTAR;

	static {
		CuisineCropType.register();
		CuisineTreeType.register();
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
		// basic tools
		{
			DelegateBlockProperties prop = DelegateBlockProperties.copy(Blocks.STONE).make(BlockBehaviour.Properties::noOcclusion);
			PAN = REGISTRATE.block("pan", p -> DelegateBlock.newBaseBlock(prop, PanBlock.TE, new PanBlock()))
					.item().defaultModel().build().blockstate((ctx, pvd) -> pvd.getMultipartBuilder(ctx.getEntry()).part()
							.modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/pan_lit")))
							.addModel().condition(BlockStateProperties.LIT, true).end())
					.addLayer(() -> RenderType::cutout).defaultLoot().tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLang().register();
			TE_PAN = REGISTRATE.blockEntity("pan", PanBlockEntity::new).validBlock(PAN)
					.renderer(() -> PanRenderer::new).register();

			JAR = REGISTRATE.block("jar", p -> DelegateBlock.newBaseBlock(prop, JarBlock.TE, new JarBlock()))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), new ModelFile.UncheckedModelFile(
							new ResourceLocation(Cuisine.MODID, "block/jar"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).simpleItem().register();
			TE_JAR = REGISTRATE.blockEntity("jar", JarBlockEntity::new).validBlock(JAR).register();

			BASIN = REGISTRATE.block("basin", p -> DelegateBlock.newBaseBlock(prop, BasinBlock.TE, new BasinBlock()))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), new ModelFile.UncheckedModelFile(
							new ResourceLocation(Cuisine.MODID, "block/basin"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).simpleItem().register();
			TE_BASIN = REGISTRATE.blockEntity("basin", BasinBlockEntity::new).validBlock(BASIN)
					.renderer(() -> BasinRenderer::new).register();

			MILL = REGISTRATE.block("mill", p -> DelegateBlock.newBaseBlock(prop, MillBlock.TE, new MillBlock()))
					.blockstate((ctx, pvd) -> {
						ModelFile base = new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/mill"));
						ModelFile top = new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/mill_top"));
						pvd.getMultipartBuilder(ctx.getEntry())
								.part().modelFile(base).addModel().end()
								.part().modelFile(top).addModel().end();
					}).tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.item().model((ctx, pvd) -> {
						ModelFile inv = new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/mill_inventory"));
						pvd.getBuilder(ctx.getName()).parent(inv);
					}).build().register();
			TE_MILL = REGISTRATE.blockEntity("mill", MillBlockEntity::new).validBlock(MILL).renderer(() -> MillRenderer::new).register();


			MORTAR = REGISTRATE.block("mortar", p -> DelegateBlock.newBaseBlock(prop, MortarBlock.TE, new MortarBlock()))
					.blockstate((ctx, pvd) -> {
						ModelFile mortar = new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/mortar"));
						ModelFile pestle = new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/pestle"));
						pvd.getMultipartBuilder(ctx.getEntry())
								.part().modelFile(mortar).addModel().end()
								.part().modelFile(pestle).addModel().end();
					}).item().defaultModel().build()
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).item().defaultModel().build().register();
			TE_MORTAR = REGISTRATE.blockEntity("mortar", MortarBlockEntity::new).validBlock(MORTAR).renderer(() -> MortarRenderer::new).register();
		}
		// fire pit
		{
			REGISTRATE.block("fire_pit", p -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion())).blockstate((ctx, pvd) -> {
				pvd.simpleBlock(ctx.getEntry(), new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/fire_pit")));
			}).simpleItem().register();
		}
	}

	public static void register() {

	}

}
