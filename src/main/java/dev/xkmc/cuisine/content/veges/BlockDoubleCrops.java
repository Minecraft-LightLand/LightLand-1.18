package dev.xkmc.cuisine.content.veges;

import dev.xkmc.cuisine.init.data.CuisineTemplates;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockDoubleCrops extends BlockCuisineCrops {

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 8);

	public BlockDoubleCrops(CuisineTemplates.Veges type, Properties props) {
		super(type, props);
	}

	/*

	public boolean isUpper(BlockState state) {
		return state.getValue(getAgeProperty()) == 8;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		if (!isUpper(state))
		super.randomTick(state, level, pos, random);
	}

	// Copied from BlockDoublePlant
	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) {
			boolean upper = isUpper(state);
			BlockPos posUp = upper ? pos : pos.up();
			BlockPos posDown = upper ? pos.down() : pos;
			Block blockUp = upper ? this : worldIn.getBlockState(posUp).getBlock();
			Block blockDown = upper ? worldIn.getBlockState(posDown).getBlock() : this;

			if (!upper)
				this.dropBlockAsItem(worldIn, pos, state, 0); // Forge move above the setting to air.

			if (blockUp == this) {
				worldIn.setBlockState(posUp, Blocks.AIR.getDefaultState(), 2);
			}

			if (blockDown == this) {
				worldIn.setBlockState(posDown, Blocks.AIR.getDefaultState(), 3);
			}
		}
	}

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getBlock() != this)
			return super.canBlockStay(worldIn, pos, state); // Forge: This function is called during world gen and
		// placement, before this block is set, so if we are not
		// 'here' then assume it's the pre-check.
		if (isUpper(state)) {
			return worldIn.getBlockState(pos.down()).getBlock() == this;
		} else {
			return worldIn.getBlockState(pos.up()).getBlock() == this && super.canBlockStay(worldIn, pos, state);
		}
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		super.grow(worldIn, rand, isUpper(state) ? pos.down() : pos, getPlantBase(state, worldIn, pos));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getBlock() == this && !isUpper(state) && (worldIn.isAirBlock(pos.up()) || worldIn.getBlockState(pos.up()).getBlock() == this)) {
			worldIn.setBlockState(pos.up(), withAge(8), 2);
		}
	}

	@Override
	public int getAge(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getPlantBase(state, world, pos).getValue(getAgeProperty());
	}

	public IBlockState getPlantBase(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (isUpper(state)) {
			IBlockState newState = world.getBlockState(pos.down());
			if (newState.getBlock() == this && !isUpper(newState)) {
				return newState;
			}
		}
		return state;
	}*/
}