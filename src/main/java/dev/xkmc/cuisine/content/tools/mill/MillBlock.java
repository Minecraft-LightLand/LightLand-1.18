package dev.xkmc.cuisine.content.tools.mill;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.l2library.block.one.RenderShapeBlockMethod;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class MillBlock implements RenderShapeBlockMethod {

	public static final BlockEntityBlockMethod<MillBlockEntity> TE = new BlockEntityBlockMethodImpl<>(CuisineBlocks.TE_MILL, MillBlockEntity.class);

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
}
