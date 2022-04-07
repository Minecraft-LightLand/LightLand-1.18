package dev.hikarishima.lightland.content.magic.block;

import dev.hikarishima.lightland.init.registrate.BlockRegistrate;
import dev.lcy0x1.block.impl.BlockEntityBlockMethodImpl;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RitualSide {
	public static final BlockEntityBlockMethod<TE> TILE_ENTITY_SUPPLIER_BUILDER = new BlockEntityBlockMethodImpl<TE>(BlockRegistrate.TE_RITUAL_SIDE, TE.class);

	@SerialClass
	public static class TE extends RitualTE {

		public TE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}
	}

}
