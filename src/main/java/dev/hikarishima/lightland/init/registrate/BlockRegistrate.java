package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.hikarishima.lightland.content.magic.block.RitualRenderer;
import dev.hikarishima.lightland.content.magic.block.RitualSide;
import dev.hikarishima.lightland.content.questline.block.LaylineHead;
import dev.hikarishima.lightland.content.questline.block.LayrootBody;
import dev.hikarishima.lightland.content.questline.block.LayrootHead;
import dev.hikarishima.lightland.init.data.GenItem;
import dev.lcy0x1.block.BlockProxy;
import dev.lcy0x1.block.DelegateBlock;
import dev.lcy0x1.block.DelegateBlockProperties;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;
import static dev.hikarishima.lightland.init.registrate.ItemRegistrate.TAB_MAIN;

/**
 * handles blocks and block entities
 */
public class BlockRegistrate {

	static {
		REGISTRATE.creativeModeTab(() -> TAB_MAIN);
	}

	private static final DelegateBlockProperties PEDESTAL = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
			.noOcclusion().lightLevel(bs -> bs.getValue(BlockStateProperties.LIT) ? 15 : 7)
			.isRedstoneConductor((a, b, c) -> false));

	public static final BlockEntry<DelegateBlock> B_RITUAL_CORE = REGISTRATE.block("ritual_core",
					(p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.ACTIVATE, RitualCore.CLICK,
							BlockProxy.TRIGGER, RitualCore.TILE_ENTITY_SUPPLIER_BUILDER))
			.blockstate((a, b) -> {
			}).defaultLoot().defaultLang().simpleItem().register();

	public static final BlockEntry<DelegateBlock> B_RITUAL_SIDE = REGISTRATE.block("ritual_side",
					(p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.CLICK, RitualSide.TILE_ENTITY_SUPPLIER_BUILDER))
			.blockstate((a, b) -> {
			}).defaultLoot().defaultLang().simpleItem().register();

	public static final BlockEntry<Block> ENCHANT_GOLD_BLOCK = REGISTRATE.block("enchant_gold_block", p ->
					new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)))
			.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();

	public static final BlockEntry<Block> LEAD_BLOCK = REGISTRATE.block("lead_block",
					p -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)))
			.defaultBlockstate().defaultLoot().defaultLang().simpleItem().register();

	public static final BlockEntry<LayrootBody> LAYROOT_BODY = REGISTRATE.block("layroot_body",
					p -> new LayrootBody(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES_PLANT)))
			.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
			.defaultLoot().defaultLang().addLayer(() -> RenderType::cutout).tag(BlockTags.CLIMBABLE).register();

	public static final BlockEntry<LayrootHead> LAYROOT_HEAD = REGISTRATE.block("layroot_head",
					p -> new LayrootHead(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES_PLANT)))
			.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
			.item().model((ctx, pvd) -> pvd.generated(ctx::getEntry, pvd.modLoc("block/" + ctx.getName()))).build()
			.defaultLoot().defaultLang().addLayer(() -> RenderType::cutout).tag(BlockTags.CLIMBABLE).register();

	public static final BlockEntry<LaylineHead> LAYLINE_HEAD = REGISTRATE.block("layline_head",
					p -> new LaylineHead(BlockBehaviour.Properties.copy(Blocks.WEEPING_VINES)))
			.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.getEntry(), pvd.models().cross(ctx.getName(), pvd.blockTexture(ctx.getEntry()))))
			.item().model((ctx, pvd) -> pvd.generated(ctx::getEntry, pvd.modLoc("block/" + ctx.getName()))).build()
			.defaultLoot().defaultLang().addLayer(() -> RenderType::cutout).tag(BlockTags.CLIMBABLE).register();

	public static final BlockEntityEntry<RitualCore.TE> TE_RITUAL_CORE = REGISTRATE.blockEntity("ritual_core", RitualCore.TE::new)
			.validBlock(B_RITUAL_CORE).renderer(() -> RitualRenderer::new).register();

	public static final BlockEntityEntry<RitualSide.TE> TE_RITUAL_SIDE = REGISTRATE.blockEntity("ritual_side", RitualSide.TE::new)
			.validBlock(B_RITUAL_SIDE).renderer(() -> RitualRenderer::new).register();


	public static final BlockEntry<Block>[] GEN_BLOCK = GenItem.genBlockMats();

	public static void register() {

	}

}
