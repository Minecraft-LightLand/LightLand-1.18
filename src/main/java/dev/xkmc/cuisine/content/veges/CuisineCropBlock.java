package dev.xkmc.cuisine.content.veges;

import dev.xkmc.cuisine.init.data.CuisineCropType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CuisineCropBlock extends CropBlock {

	static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	private static final ThreadLocal<CuisineCropType> TEMP = new ThreadLocal<>();

	private static Properties warp(CuisineCropType type, Properties props) {
		TEMP.set(type);
		return props;
	}

	private CuisineCropType type;

	public CuisineCropBlock(CuisineCropType type, Properties props) {
		super(warp(type, props));
		getType();
	}

	public CuisineCropType getType() {
		if (this.type != null) {
			return type;
		}
		type = TEMP.get();
		TEMP.set(null);
		return type;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return getType().getEntry().get();
	}

	@Override
	public IntegerProperty getAgeProperty() {
		return getType().getAge();
	}

	@Override
	public int getMaxAge() {
		return getType().getMaxAge();
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(getAgeProperty());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext col) {
		return SHAPE_BY_AGE[state.getValue(getAgeProperty())];
	}

}