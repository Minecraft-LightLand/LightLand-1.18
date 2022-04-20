package dev.xkmc.cuisine.content.tools.mill;

import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.lcy0x1.block.one.RenderShapeBlockMethod;
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
