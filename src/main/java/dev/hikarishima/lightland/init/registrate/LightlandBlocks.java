package dev.hikarishima.lightland.init.registrate;

import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.hikarishima.lightland.content.magic.block.RitualRenderer;
import dev.hikarishima.lightland.content.magic.block.RitualSide;
import dev.hikarishima.lightland.content.questline.block.*;
import dev.hikarishima.lightland.init.data.GenItem;
import dev.xkmc.l2library.block.BlockProxy;
import dev.xkmc.l2library.block.DelegateBlock;
import dev.xkmc.l2library.block.DelegateBlockProperties;
import dev.xkmc.l2library.repack.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntityEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;
import static dev.hikarishima.lightland.init.registrate.LightlandItems.TAB_MAIN;

/**
 * handles blocks and block entities
 */
public class LightlandBlocks {

	static {
		REGISTRATE.creativeModeTab(() -> TAB_MAIN);
	}

	public static final BlockEntry<DelegateBlock> B_RITUAL_CORE, B_RITUAL_SIDE;
	public static final BlockEntry<Block> ENCHANT_GOLD_BLOCK, LEAD_BLOCK, MAGICIUM_BLOCK;
	public static final BlockEntry<LayrootBody> LAYROOT_BODY;
	public static final BlockEntry<LayrootHead> LAYROOT_HEAD;
	public static final BlockEntry<LaylineHead> LAYLINE_HEAD;
	public static final BlockEntry<LaylineChargerBlock> LAYLINE_CHARGER;
	public static final BlockEntry<SlimeCarpet> SLIME_CARPET;
	public static final BlockEntry<WebBlock> SLIME_VINE;

	public static final BlockEntry<DelegateBlock> MAZE_WALL;

	public static final BlockEntityEntry<RitualCore.TE> TE_RITUAL_CORE;
	public static final BlockEntityEntry<RitualSide.TE> TE_RITUAL_SIDE;


	public static final BlockEntry<Block>[] GEN_BLOCK = GenItem.genBlockMats();

	static {
		{
			DelegateBlockProperties PEDESTAL = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
					.noOcclusion().lightLevel(bs -> bs.getValue(BlockStateProperties.LIT) ? 15 : 7)
					.isRedstoneConductor((a, b, c) -> false));
			B_RITUAL_CORE = REGISTRATE.block("ritual_core",
							(p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.ACTIVATE, RitualCore.CLICK,
									BlockProxy.TRIGGER, RitualCore.TILE_ENTITY_SUPPLIER_BUILDER))
					.blockstate((a, b) -> {
					}).tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang().simpleItem().register();
			B_RITUAL_SIDE = REGISTRATE.block("ritual_side",
							(p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.CLICK, RitualSide.TILE_ENTITY_SUPPLIER_BUILDER))
					.blockstate((a, b) -> {
					}).tag(BlockTags.MINEABLE_WITH_PICKAXE).defaultLoot().defaultLang().simpleItem().register();
			TE_RITUAL_CORE = REGISTRATE.blockEntity("ritual_core", RitualCore.TE::new)
					.validBlock(B_RITUAL_CORE).renderer(() -> RitualRenderer::new).register();
			TE_RITUAL_SIDE = REGISTRATE.blockEntity("ritual_side", RitualSide.TE::new)
					.validBlock(B_RITUAL_SIDE).renderer(() -> RitualRenderer::new).register();
		}
		{
			ENCHANT_GOLD_BLOCK = REGISTRATE.block("enchant_gold_block", p ->
							new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
			LEAD_BLOCK = REGISTRATE.block("lead_block",
							p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
			MAGICIUM_BLOCK = REGISTRATE.block("magicium_block",
							p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
		}
		{
			BlockBehaviour.Properties prop_root = BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.WOOD)
					.noCollission().instabreak();
			BlockBehaviour.Properties prop_line = BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.WOOD)
					.noCollission().instabreak().randomTicks();
			BlockBehaviour.Properties prop_charger = BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK);
			LAYROOT_HEAD = REGISTRATE.block("layroot_head", p -> new LayrootHead(prop_root))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
					.item().model((ctx, pvd) -> pvd.generated(ctx::getEntry, pvd.modLoc("block/" + ctx.getName()))).build()
					.defaultLoot().defaultLang().addLayer(() -> RenderType::cutout).tag(BlockTags.CLIMBABLE).register();
			LAYROOT_BODY = REGISTRATE.block("layroot_body", p -> new LayrootBody(prop_root))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
					.loot((table, block) -> table.dropOther(block, LAYROOT_HEAD.get()))
					.defaultLang().addLayer(() -> RenderType::cutout).tag(BlockTags.CLIMBABLE).register();
			LAYLINE_HEAD = REGISTRATE.block("layline_head", p -> new LaylineHead(prop_line))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
					.item().model((ctx, pvd) -> pvd.generated(ctx::getEntry, pvd.modLoc("block/" + ctx.getName()))).build()
					.defaultLoot().defaultLang().addLayer(() -> RenderType::cutout).tag(BlockTags.CLIMBABLE).register();
			LAYLINE_CHARGER = REGISTRATE.block("layline_charger", LaylineChargerBlock::new)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.defaultBlockstate().simpleItem().defaultLoot().defaultLang().register();
			BlockBehaviour.Properties prop_slime = BlockBehaviour.Properties.of(Material.WEB).noCollission().strength(4.0F);
			SLIME_CARPET = REGISTRATE.block("slime_carpet", p -> new SlimeCarpet(prop_slime))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().carpet(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
					.tag(BlockTags.MINEABLE_WITH_HOE)
					.simpleItem().loot(RegistrateBlockLootTables::dropWhenSilkTouch).defaultLang().register();
			SLIME_VINE = REGISTRATE.block("slime_vine", p -> new WebBlock(prop_slime))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
					.item().model((ctx, pvd) -> pvd.generated(ctx::getEntry, pvd.modLoc("block/" + ctx.getName()))).build()
					.tag(BlockTags.MINEABLE_WITH_HOE)
					.loot(RegistrateBlockLootTables::dropWhenSilkTouch).addLayer(() -> RenderType::cutout).defaultLang().register();

		}
		{
			DelegateBlockProperties BP_METAL = DelegateBlockProperties.copy(Blocks.OBSIDIAN).make(BlockBehaviour.Properties::noDrops);

			MAZE_WALL = REGISTRATE.block("maze_wall", p -> DelegateBlock.newBaseBlock(BP_METAL,
							MazeWallBlock.NEIGHBOR, MazeWallBlock.ALL_DIRE_STATE))
					.blockstate((ctx, pvd) -> {
					}).loot((table, block) -> table.accept((rl, b) -> b.build()))
					.tag(BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
					.defaultLang().simpleItem().register();
		}
	}

	public static void register() {
	}

}
