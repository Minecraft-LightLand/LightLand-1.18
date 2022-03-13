package dev.lcy0x1.block;

import dev.lcy0x1.block.impl.AllDireBlockMethodImpl;
import dev.lcy0x1.block.impl.HorizontalBlockMethodImpl;
import dev.lcy0x1.block.impl.PowerBlockMethodImpl;
import dev.lcy0x1.block.impl.TriggerBlockMethodImpl;
import dev.lcy0x1.block.type.BlockMethod;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class BlockProxy {

	public static final BlockMethod POWER = new PowerBlockMethodImpl();
	public static final BlockMethod ALL_DIRECTION = new AllDireBlockMethodImpl();
	public static final BlockMethod HORIZONTAL = new HorizontalBlockMethodImpl();
	public static final BlockMethod TRIGGER = new TriggerBlockMethodImpl(4);

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

}
