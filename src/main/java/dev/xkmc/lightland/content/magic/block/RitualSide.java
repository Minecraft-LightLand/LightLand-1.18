package dev.xkmc.lightland.content.magic.block;

import dev.xkmc.l2library.block.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2library.block.one.BlockEntityBlockMethod;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lightland.init.registrate.LightlandBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RitualSide {
	public static final BlockEntityBlockMethod<TE> TILE_ENTITY_SUPPLIER_BUILDER = new BlockEntityBlockMethodImpl<TE>(LightlandBlocks.TE_RITUAL_SIDE, TE.class);

	@SerialClass
	public static class TE extends RitualTE {

		public TE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}
	}

}
