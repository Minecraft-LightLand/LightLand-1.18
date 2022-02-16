package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.hikarishima.lightland.content.magic.block.RitualRenderer;
import dev.hikarishima.lightland.content.magic.block.RitualSide;
import dev.lcy0x1.block.BlockProxy;
import dev.lcy0x1.block.DelegateBlock;
import dev.lcy0x1.block.DelegateBlockProperties;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

/**
 * handles blocks and block entities
 */
public class BlockRegistrate {

    private static final DelegateBlockProperties PEDESTAL = DelegateBlockProperties.copy(Blocks.STONE).make(e -> e
            .noOcclusion().lightLevel(bs -> bs.getValue(BlockStateProperties.LIT) ? 15 : 7)
            .isRedstoneConductor((a, b, c) -> false));

    public static final BlockEntry<DelegateBlock> B_RITUAL_CORE = REGISTRATE.block("ritual_core",
                    (p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.ACTIVATE, RitualCore.CLICK,
                            BlockProxy.TRIGGER, RitualCore.TILE_ENTITY_SUPPLIER_BUILDER))
            .defaultLoot().defaultLang().simpleItem().register();

    public static final BlockEntry<DelegateBlock> B_RITUAL_SIDE = REGISTRATE.block("ritual_side",
                    (p) -> DelegateBlock.newBaseBlock(PEDESTAL, RitualCore.CLICK, RitualSide.TILE_ENTITY_SUPPLIER_BUILDER))
            .defaultLoot().defaultLang().simpleItem().register();

    public static final BlockEntityEntry<RitualCore.TE> TE_RITUAL_CORE = REGISTRATE.blockEntity("ritual_core", RitualCore.TE::new)
            .validBlock(B_RITUAL_CORE).renderer(() -> RitualRenderer::new).register();

    public static final BlockEntityEntry<RitualSide.TE> TE_RITUAL_SIDE = REGISTRATE.blockEntity("ritual_side", RitualSide.TE::new)
            .validBlock(B_RITUAL_SIDE).renderer(() -> RitualRenderer::new).register();

    public static void register() {

    }

}
