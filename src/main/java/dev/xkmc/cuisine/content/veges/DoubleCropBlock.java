package dev.xkmc.cuisine.content.veges;

import dev.xkmc.cuisine.init.data.CuisineCropType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nullable;
import java.util.Random;

public class DoubleCropBlock extends CuisineCropBlock {

	public DoubleCropBlock(CuisineCropType type, Properties props) {
		super(type, props);
	}

	public boolean isLower(BlockState state) {
		return state.is(this) && state.getValue(getAgeProperty()) <= getMaxAge();
	}

	public BlockState updateShape(BlockState state_0, Direction direction, BlockState state_1, LevelAccessor level, BlockPos pos_0, BlockPos pos_1) {
		if (isLower(state_0)) {
			if (!hasUpper(state_0))
				return super.updateShape(state_0, direction, state_1, level, pos_0, pos_1);
			BlockState up = level.getBlockState(pos_0.above());
			if (up.is(this) && !isLower(up))
				return super.updateShape(state_0, direction, state_1, level, pos_0, pos_1);
		} else {
			BlockState low = level.getBlockState(pos_0.below());
			if (low.is(this) && isLower(low))
				return super.updateShape(state_0, direction, state_1, level, pos_0, pos_1);
		}
		if (state_0.getFluidState().is(Fluids.WATER)) {
			if (level instanceof ServerLevel server)
				Block.dropResources(state_0, server, pos_0, null);
			return Blocks.WATER.defaultBlockState();
		}
		return Blocks.AIR.defaultBlockState();
	}

	protected boolean hasUpper(BlockState state_0) {
		return true;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		return blockpos.getY() < level.getMaxBuildHeight() - 1 &&
				level.getBlockState(blockpos.above()).canBeReplaced(context) ?
				super.getStateForPlacement(context) : null;
	}

	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		BlockPos blockpos = pos.above();
		level.setBlock(blockpos, this.defaultBlockState().setValue(getAgeProperty(), getMaxAge() + 1), 3);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (isLower(state)) {
			return super.canSurvive(state, level, pos);
		} else {
			BlockState blockstate = level.getBlockState(pos.below());
			if (state.getBlock() != this)
				return super.canSurvive(state, level, pos);
			return blockstate.is(this) && isLower(blockstate);
		}
	}

	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide) {
			if (player.isCreative()) {
				preventCreativeDropFromBottomPart(level, pos, state, player);
			}
		}
		super.playerWillDestroy(level, pos, state, player);
	}

	protected void preventCreativeDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
		if (!isLower(state)) {
			BlockPos blockpos = pos.below();
			BlockState blockstate = level.getBlockState(blockpos);
			if (blockstate.is(state.getBlock()) && isLower(blockstate)) {
				BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
				level.setBlock(blockpos, blockstate1, 35);
				level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
			}
		}
	}

	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return false;
	}

	public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean client) {
		if (isLower(state)) return super.isValidBonemealTarget(level, pos, state, client);
		BlockState lower = level.getBlockState(pos.below());
		return lower.is(this) && super.isValidBonemealTarget(level, pos.below(), lower, client);
	}

	@Override
	public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
		if (isLower(state)) super.performBonemeal(level, random, pos, state);
		BlockState lower = level.getBlockState(pos.below());
		if (lower.is(this)) super.performBonemeal(level, random, pos.below(), lower);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return isLower(state) && super.isRandomlyTicking(state);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		if (isLower(state))
			super.randomTick(state, level, pos, random);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext col) {
		return Shapes.block();
	}

	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(Blocks.FARMLAND) && (getType().type != CuisineCropType.ModelType.WATER || level.getFluidState(pos.above()).is(Fluids.WATER));
	}

	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return getType().type == CuisineCropType.ModelType.WATER ? PlantType.WATER : PlantType.CROP;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return !isLower(state) || getType().type != CuisineCropType.ModelType.WATER ? super.getFluidState(state) : Fluids.WATER.getSource(false);
	}

	@Override
	protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
		if (isLower(state))
			super.spawnDestroyParticles(level, player, pos, state);
	}
}