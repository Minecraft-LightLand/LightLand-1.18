package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.hikarishima.lightland.content.magic.block.RitualRenderer;
import dev.hikarishima.lightland.content.magic.block.RitualSide;
import dev.hikarishima.lightland.content.questline.block.*;
import dev.hikarishima.lightland.init.data.GenItem;
import dev.lcy0x1.block.BlockProxy;
import dev.lcy0x1.block.DelegateBlock;
import dev.lcy0x1.block.DelegateBlockProperties;
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
import static dev.hikarishima.lightland.init.registrate.ItemRegistrate.TAB_MAIN;

/**
 * handles blocks and block entities
 */
public class BlockRegistrate {

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
					}).defaultLoot().defaultLang().simpleItem().register();
			B_RITUAL_SIDE = REGISTRATE.block("ritual_side",
							(p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.CLICK, RitualSide.TILE_ENTITY_SUPPLIER_BUILDER))
					.blockstate((a, b) -> {
					}).defaultLoot().defaultLang().simpleItem().register();
			TE_RITUAL_CORE = REGISTRATE.blockEntity("ritual_core", RitualCore.TE::new)
					.validBlock(B_RITUAL_CORE).renderer(() -> RitualRenderer::new).register();
			TE_RITUAL_SIDE = REGISTRATE.blockEntity("ritual_side", RitualSide.TE::new)
					.validBlock(B_RITUAL_SIDE).renderer(() -> RitualRenderer::new).register();
		}
		{
			ENCHANT_GOLD_BLOCK = REGISTRATE.block("enchant_gold_block", p ->
							new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)))
					.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
			LEAD_BLOCK = REGISTRATE.block("lead_block",
							p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
					.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();
			MAGICIUM_BLOCK = REGISTRATE.block("magicium_block",
							p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
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
					.defaultBlockstate().simpleItem().defaultLoot().defaultLang().register();
			BlockBehaviour.Properties prop_slime = BlockBehaviour.Properties.of(Material.WEB).noCollission().strength(4.0F);
			SLIME_CARPET = REGISTRATE.block("slime_carpet", p -> new SlimeCarpet(prop_slime))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().carpet(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
					.simpleItem().loot(RegistrateBlockLootTables::dropWhenSilkTouch).defaultLang().register();
			SLIME_VINE = REGISTRATE.block("slime_vine", p -> new WebBlock(prop_slime))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
					.item().model((ctx, pvd) -> pvd.generated(ctx::getEntry, pvd.modLoc("block/" + ctx.getName()))).build()
					.loot(RegistrateBlockLootTables::dropWhenSilkTouch).addLayer(() -> RenderType::cutout).defaultLang().register();

		}
	}

	public static void register() {
	}

}
