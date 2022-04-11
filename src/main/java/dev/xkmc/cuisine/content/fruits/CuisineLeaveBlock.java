package dev.xkmc.cuisine.content.fruits;

import dev.xkmc.cuisine.init.data.CuisineTreeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CuisineLeaveBlock extends LeavesBlock {

	private static final ThreadLocal<CuisineTreeType> TEMP = new ThreadLocal<>();

	private static Properties warp(CuisineTreeType type, Properties props) {
		TEMP.set(type);
		return props;
	}

	public static BooleanProperty CORE = BooleanProperty.create("core");
	public static IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

	private CuisineTreeType type;

	public CuisineLeaveBlock(CuisineTreeType type, Properties props) {
		super(warp(type, props));
		getType();
		registerDefaultState(defaultBlockState().setValue(CORE, false).setValue(AGE, 1));
	}

	public CuisineTreeType getType() {
		if (this.type != null) {
			return type;
		}
		type = TEMP.get();
		TEMP.set(null);
		return type;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(CORE, AGE);
	}

}
