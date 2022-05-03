package dev.xkmc.cuisine.init.registrate;

import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntityEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.block.BlockProxy;
import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.block.DelegateBlockProperties;
import dev.xkmc.l2library.util.LootTableTemplate;
import dev.xkmc.cuisine.content.tools.base.CuisineUtil;
import dev.xkmc.cuisine.content.tools.basin.BasinBlock;
import dev.xkmc.cuisine.content.tools.basin.BasinBlockEntity;
import dev.xkmc.cuisine.content.tools.basin.BasinRenderer;
import dev.xkmc.cuisine.content.tools.firepit.FirePitBlock;
import dev.xkmc.cuisine.content.tools.firepit.stick.FirePitStickBlockEntity;
import dev.xkmc.cuisine.content.tools.firepit.stick.FirePitStickRenderer;
import dev.xkmc.cuisine.content.tools.firepit.wok.FirePitWokBlockEntity;
import dev.xkmc.cuisine.content.tools.firepit.wok.FirePitWokRenderer;
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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

@SuppressWarnings({"rawtype", "unsafe"})
public class CuisineBlocks {

	static {
		REGISTRATE.creativeModeTab(() -> CuisineItems.TAB_MAIN);
	}

	public static final BlockEntry<DelegateBlock> PAN, JAR, BASIN, MILL, MORTAR,
			FIRE_PIT, FIRE_PIT_STICK, FIRE_PIT_WOK, WOK;

	public static final BlockEntityEntry<PanBlockEntity> TE_PAN;
	public static final BlockEntityEntry<JarBlockEntity> TE_JAR;
	public static final BlockEntityEntry<BasinBlockEntity> TE_BASIN;
	public static final BlockEntityEntry<MillBlockEntity> TE_MILL;
	public static final BlockEntityEntry<MortarBlockEntity> TE_MORTAR;
	public static final BlockEntityEntry<FirePitStickBlockEntity> TE_STICK;
	public static final BlockEntityEntry<FirePitWokBlockEntity> TE_WOK;

	static {
		CuisineCropType.register();
		CuisineTreeType.register();
		WoodType.register();

		// basic tools
		DelegateBlockProperties prop = DelegateBlockProperties.copy(Blocks.STONE).make(BlockBehaviour.Properties::noOcclusion);
		{
			PAN = REGISTRATE.block("pan", p -> DelegateBlock.newBaseBlock(prop,
							PanBlock.TE, new PanBlock(), CuisineUtil.FIRE, CuisineUtil.TAKE, CuisineUtil.LID, CuisineUtil.DUMP, CuisineUtil.ADD))
					.item().defaultModel().build().blockstate((ctx, pvd) -> pvd.getMultipartBuilder(ctx.getEntry()).part()
							.modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/pan_lit")))
							.addModel().condition(BlockStateProperties.LIT, true).end()).addLayer(() -> RenderType::cutout)
					.defaultLoot().tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLang().register();
			TE_PAN = REGISTRATE.blockEntity("pan", PanBlockEntity::new).validBlock(PAN)
					.renderer(() -> PanRenderer::new).register();

			JAR = REGISTRATE.block("jar", p -> DelegateBlock.newBaseBlock(prop,
							JarBlock.TE, CuisineUtil.TIME, CuisineUtil.DUMP, CuisineUtil.ADD))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), new ModelFile.UncheckedModelFile(
							new ResourceLocation(Cuisine.MODID, "block/jar"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).simpleItem().register();
			TE_JAR = REGISTRATE.blockEntity("jar", JarBlockEntity::new).validBlock(JAR).register();

			BASIN = REGISTRATE.block("basin", p -> DelegateBlock.newBaseBlock(prop,
							BasinBlock.TE, new BasinBlock(), CuisineUtil.TIME, CuisineUtil.DUMP, CuisineUtil.ADD))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), new ModelFile.UncheckedModelFile(
							new ResourceLocation(Cuisine.MODID, "block/basin"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).simpleItem().register();
			TE_BASIN = REGISTRATE.blockEntity("basin", BasinBlockEntity::new).validBlock(BASIN)
					.renderer(() -> BasinRenderer::new).register();

			MILL = REGISTRATE.block("mill", p -> DelegateBlock.newBaseBlock(prop,
							MillBlock.TE, new MillBlock(),
							CuisineUtil.DUMP, CuisineUtil.ADD, CuisineUtil.STEP))
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

			MORTAR = REGISTRATE.block("mortar", p -> DelegateBlock.newBaseBlock(prop,
							MortarBlock.TE, BlockProxy.HORIZONTAL, new MortarBlock(),
							CuisineUtil.DUMP, CuisineUtil.ADD, CuisineUtil.STEP))
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
			FIRE_PIT = REGISTRATE.block("fire_pit", p -> DelegateBlock.newBaseBlock(prop,
							BlockProxy.HORIZONTAL, FirePitBlock.SHAPE_BASE, CuisineUtil.FIRE, FirePitBlock.PUT, FirePitBlock.BURN))
					.blockstate(CuisineBlocks::makeFirePit).addLayer(() -> RenderType::cutout)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLang().register();

			FIRE_PIT_STICK = REGISTRATE.block("fire_pit_with_sticks", p -> DelegateBlock.newBaseBlock(prop,
							BlockProxy.HORIZONTAL, FirePitBlock.SHAPE_BASE, CuisineUtil.FIRE, FirePitBlock.BURN,
							FirePitBlock.STICK, CuisineUtil.DUMP, CuisineUtil.ADD))
					.blockstate(CuisineBlocks::makeFirePit).addLayer(() -> RenderType::cutout)
					.loot((table, block) -> table.add(block, LootTableTemplate.selfOrOther(block, FIRE_PIT.get(), Items.STICK, 3)))
					.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLang().register();

			WOK = REGISTRATE.block("wok", p -> DelegateBlock.newBaseBlock(prop, BlockProxy.HORIZONTAL, FirePitBlock.SHAPE_WOK))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.getEntry(), new ModelFile.UncheckedModelFile(
							new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName()))))
					.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLang().register();

			FIRE_PIT_WOK = REGISTRATE.block("fire_pit_with_wok", p -> DelegateBlock.newBaseBlock(prop,
							BlockProxy.HORIZONTAL, FirePitBlock.SHAPE_BASE_AND_WOK, CuisineUtil.FIRE, FirePitBlock.BURN,
							FirePitBlock.WOK, CuisineUtil.DUMP, CuisineUtil.ADD))
					.blockstate(CuisineBlocks::makeFirePit).addLayer(() -> RenderType::cutout)
					.loot((table, block) -> table.add(block, LootTableTemplate.selfOrOther(block, FIRE_PIT.get(), WOK.get().asItem(), 1)))
					.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLang().register();

			TE_STICK = REGISTRATE.blockEntity("fire_pit_with_stick", FirePitStickBlockEntity::new)
					.validBlock(FIRE_PIT_STICK).renderer(() -> FirePitStickRenderer::new).register();
			TE_WOK = REGISTRATE.blockEntity("fire_pit_with_wok", FirePitWokBlockEntity::new)
					.validBlock(FIRE_PIT_WOK).renderer(() -> FirePitWokRenderer::new).register();

		}
	}

	private static void makeFirePit(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		ModelFile fire = new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/pan_lit"));
		ModelFile base = new ModelFile.UncheckedModelFile(new ResourceLocation(Cuisine.MODID, "block/" + ctx.getName()));
		MultiPartBlockStateBuilder builder = pvd.getMultipartBuilder(ctx.getEntry());
		builder.part().modelFile(fire).addModel().condition(BlockStateProperties.LIT, true).end();
		for (Direction dire : Direction.Plane.HORIZONTAL.stream().toList())
			builder.part().modelFile(base).rotationY((int) dire.toYRot()).addModel()
					.condition(BlockStateProperties.HORIZONTAL_FACING, dire).end();
	}

	public static void register() {

	}

}
