package dev.xkmc.cuisine.content.fruits;

import dev.xkmc.cuisine.init.data.CuisineTreeType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Random;

public class CuisineLeaveBlock extends VanillaLeavesBlock implements BonemealableBlock {

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
		registerDefaultState(defaultBlockState().setValue(CORE, true).setValue(AGE, 1));
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

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		super.randomTick(state, level, pos, random);
		if (state.getValue(CORE)) {
			if (random.nextDouble() > 0.9) { // TODO put it in config
				grow(level, random, pos, state, false);
			}
		}
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean client) {
		return state.getValue(CORE);
	}

	@Override
	public boolean isBonemealSuccess(Level p_50901_, Random p_50902_, BlockPos p_50903_, BlockState p_50904_) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
		grow(level, random, pos, state, true);
	}

	public void grow(ServerLevel level, Random random, BlockPos pos, BlockState state, boolean bonemeal) {
		int age = state.getValue(AGE);
		if (age < 3) {
			age = Math.min(3, age + (bonemeal ? random.nextInt(1, 3) : 1));
			state = state.setValue(AGE, age);
			if (age == 3 && bonemeal && random.nextDouble() > 0.9) // TODO config
				state = state.setValue(CORE, false);
			level.setBlockAndUpdate(pos, state);
		} else {
			level.setBlockAndUpdate(pos, state.setValue(AGE, 0));
			dropFruit(level, pos, getType().getFruit().asStack());
		}
	}

	private static void dropFruit(Level level, BlockPos pos, ItemStack stack) {
		float f1 = EntityType.ITEM.getHeight() / 2.0F;
		if (!level.isClientSide && !stack.isEmpty()) {
			ItemEntity itementity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5 - f1, pos.getZ() + 0.5, stack);
			itementity.setDefaultPickUpDelay();
			level.addFreshEntity(itementity);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
		return super.getStateForPlacement(placeContext).setValue(CORE, false).setValue(AGE, 0);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (state.getValue(AGE) == 3) {
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, state.setValue(AGE, 0));
				dropFruit(level, pos, getType().getFruit().asStack());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
