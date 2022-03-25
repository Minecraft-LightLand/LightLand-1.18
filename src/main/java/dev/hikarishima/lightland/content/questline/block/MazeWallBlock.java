package dev.hikarishima.lightland.content.questline.block;

import dev.hikarishima.lightland.content.questline.mobs.cursedknight.BaseCursedKnight;
import dev.hikarishima.lightland.init.registrate.EntityRegistrate;
import dev.hikarishima.lightland.init.registrate.ItemRegistrate;
import dev.lcy0x1.block.mult.*;
import dev.lcy0x1.block.one.MirrorRotateBlockMethod;
import dev.lcy0x1.block.one.PushReactionBlockMethod;
import dev.lcy0x1.block.one.SpecialDropBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MazeWallBlock {

	public static final Neighbor NEIGHBOR = new Neighbor();
	public static final AllDireState ALL_DIRE_STATE = new AllDireState();
	public static final Spawner SPAWNER = new Spawner();

	public static final int DELAY = 4;

	public static class Neighbor implements NeighborUpdateBlockMethod {

		@Override
		public void neighborChanged(Block self, BlockState state, Level level, BlockPos pos, Block nei_block, BlockPos nei_pos, boolean moving) {
			if (level.isClientSide())
				return;
			level.scheduleTick(pos, self, DELAY);
		}
	}

	public static class Spawner implements NeighborUpdateBlockMethod, DefaultStateBlockMethod, ScheduleTickBlockMethod, CreateBlockStateBlockMethod {

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(BlockStateProperties.POWERED);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			return state.setValue(BlockStateProperties.POWERED, false);
		}

		@Override
		public void neighborChanged(Block self, BlockState state, Level level, BlockPos pos, Block nei_block, BlockPos nei_pos, boolean moving) {
			if (level.isClientSide())
				return;
			boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
			if (state.getValue(BlockStateProperties.POWERED) != flag)
				level.scheduleTick(pos, state.getBlock(), 2);
		}

		@Override
		public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
			boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
			if (state.getValue(BlockStateProperties.POWERED) != flag)
				level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, flag));
			if (!flag)
				return;
			int height = 3;
			int radius = 7;
			int count = level.getEntities(EntityTypeTest.forClass(BaseCursedKnight.class),
					new AABB(pos.above(height - 1)).inflate(radius, height, radius), e -> true).size();
			if (count > 6)
				return;
			spawn(EntityRegistrate.ET_CURSED_KNIGHT.get(), level, pos.offset(0, 1, 0));
			spawn(EntityRegistrate.ET_CURSED_SHIELD.get(), level, pos.offset(0, 1, 1));
			spawn(EntityRegistrate.ET_CURSED_ARCHER.get(), level, pos.offset(0, 1, -1));
		}

		private void spawn(EntityType<? extends BaseCursedKnight<?>> type, ServerLevel level, BlockPos pos) {
			BaseCursedKnight<?> e = type.create(level);
			e.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.SPAWNER, null, null);
			level.addFreshEntity(e);
		}

	}

	public static class AllDireState implements
			CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod,
			ScheduleTickBlockMethod, MirrorRotateBlockMethod, OnClickBlockMethod, PushReactionBlockMethod,
			SpecialDropBlockMethod {

		public static final BooleanProperty[] PROPS = {BlockStateProperties.DOWN, BlockStateProperties.UP,
				BlockStateProperties.NORTH, BlockStateProperties.SOUTH,
				BlockStateProperties.WEST, BlockStateProperties.EAST};

		private static final Map<Direction, BooleanProperty> MAP = Map.of(
				Direction.DOWN, BlockStateProperties.DOWN,
				Direction.UP, BlockStateProperties.UP,
				Direction.NORTH, BlockStateProperties.NORTH,
				Direction.SOUTH, BlockStateProperties.SOUTH,
				Direction.WEST, BlockStateProperties.WEST,
				Direction.EAST, BlockStateProperties.EAST);

		protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(PROPS);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			for (BooleanProperty bp : PROPS)
				state = state.setValue(bp, false);
			return state;
		}

		@Override
		public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
			BlockState rep = getDefaultState(state);
			BlockState self = state;
			for (Direction dire : Direction.values()) {
				BlockPos next = pos.relative(dire);
				if (level.isOutsideBuildHeight(next))
					continue;
				BlockState nei = level.getBlockState(next);
				if (self.getValue(MAP.get(dire))) {
					if (nei.getBlock() != self.getBlock()) {
						nei = getStateForPlacement(rep, level, next);
						level.setBlockAndUpdate(next, nei);
					}
				} else if (nei.getBlock() == self.getBlock()) {
					self = self.setValue(MAP.get(dire), true);
				}
			}
			if (self != state)
				level.setBlockAndUpdate(pos, self);
		}

		@Override
		public BlockState mirror(BlockState state, Mirror mirrorIn) {
			BlockState ans = state;
			for (int i = 2; i < 6; i++) {
				Direction d0 = Direction.values()[i];
				Direction d1 = mirrorIn.mirror(d0);
				ans = ans.setValue(PROPS[d1.ordinal()], state.getValue(PROPS[d0.ordinal()]));
			}
			return ans;
		}

		@Override
		public BlockState rotate(BlockState state, Rotation rot) {
			BlockState ans = state;
			for (int i = 2; i < 6; i++) {
				Direction d0 = Direction.values()[i];
				Direction d1 = rot.rotate(d0);
				ans = ans.setValue(PROPS[d1.ordinal()], state.getValue(PROPS[d0.ordinal()]));
			}
			return ans;
		}

		@Override
		public BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
			Level level = context.getLevel();
			BlockPos pos = context.getClickedPos();
			return getStateForPlacement(def, level, pos);
		}

		private BlockState getStateForPlacement(BlockState def, Level level, BlockPos pos) {
			for (Direction dire : Direction.values()) {
				BlockState nei = level.getBlockState(pos.relative(dire));
				if (nei.getBlock() == def.getBlock()) {
					def = def.setValue(MAP.get(dire), true);
				}
			}
			return def;
		}

		@Override
		public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
			ItemStack stack = player.getItemInHand(hand);
			if (stack.is(ItemRegistrate.DISPELL_DUST.get())) {
				if (!level.isClientSide()) {
					for (Direction dire : Direction.values()) {
						BlockPos next = pos.relative(dire);
						if (level.isOutsideBuildHeight(next))
							continue;
						BlockState nei = level.getBlockState(next);
						if (nei.getBlock() == state.getBlock()) {
							level.setBlockAndUpdate(next, nei.setValue(MAP.get(dire.getOpposite()), false));
						}
					}
					level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					if (!player.getAbilities().instabuild)
						stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		}

		@Override
		public PushReaction getPistonPushReaction(BlockState state) {
			return PushReaction.BLOCK;
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
			for (Direction dire : Direction.values()) {
				if (state.getValue(MAP.get(dire))) {
					return List.of();
				}
			}
			return List.of(new ItemStack(state.getBlock()));
		}
	}

}
