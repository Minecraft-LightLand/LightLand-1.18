package dev.lcy0x1.block;

import dev.lcy0x1.block.mult.*;
import dev.lcy0x1.block.one.BlockEntityBlockMethod;
import dev.lcy0x1.block.one.BlockPowerBlockMethod;
import dev.lcy0x1.block.one.LightBlockMethod;
import dev.lcy0x1.block.one.MirrorRotateBlockMethod;
import dev.lcy0x1.block.type.BlockMethod;
import dev.lcy0x1.block.type.MultipleBlockMethod;
import dev.lcy0x1.block.type.SingletonBlockMethod;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DelegateBlockImpl extends DelegateBlock {

	private static final ThreadLocal<BlockImplementor> TEMP = new ThreadLocal<>();

	BlockImplementor impl;

	protected DelegateBlockImpl(DelegateBlockProperties p, BlockMethod... impl) {
		super(handler(construct(p).addImpls(impl)));
		registerDefaultState(this.impl.execute(DefaultStateBlockMethod.class).reduce(defaultBlockState(),
				(state, def) -> def.getDefaultState(state), (a, b) -> a));
	}

	public static BlockImplementor construct(DelegateBlockProperties bb) {
		return new BlockImplementor(bb.getProps());
	}

	private static Properties handler(BlockImplementor bi) {
		if (TEMP.get() != null)
			throw new RuntimeException("concurrency error");
		TEMP.set(bi);
		return bi.props;
	}

	@Override
	public final boolean isSignalSource(BlockState bs) {
		return impl.one(BlockPowerBlockMethod.class).isPresent();
	}

	@Override
	public final int getLightEmission(BlockState bs, BlockGetter w, BlockPos pos) {
		return impl.one(LightBlockMethod.class).map(e -> e.getLightValue(bs, w, pos))
				.orElse(super.getLightEmission(bs, w, pos));
	}

	@Override
	public final BlockState getStateForPlacement(BlockPlaceContext context) {
		return impl.execute(PlacementBlockMethod.class).reduce(defaultBlockState(),
				(state, impl) -> impl.getStateForPlacement(state, context), (a, b) -> a);
	}

	@Override
	public final int getSignal(BlockState bs, BlockGetter r, BlockPos pos, Direction d) {
		return impl.one(BlockPowerBlockMethod.class)
				.map(e -> e.getSignal(bs, r, pos, d))
				.orElse(0);
	}

	@Override
	public final BlockState mirror(BlockState state, Mirror mirrorIn) {
		return impl.one(MirrorRotateBlockMethod.class).map(e -> e.mirror(state, mirrorIn)).orElse(state);
	}

	@Override
	public final InteractionResult use(BlockState bs, Level w, BlockPos pos, Player pl, InteractionHand h, BlockHitResult r) {
		return impl.execute(OnClickBlockMethod.class)
				.map(e -> e.onClick(bs, w, pos, pl, h, r))
				.filter(e -> e != InteractionResult.PASS)
				.findFirst().orElse(InteractionResult.PASS);
	}

	@Override
	public final void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (impl.one(BlockEntityBlockMethod.class).isPresent() && state.getBlock() != newState.getBlock()) {
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity != null) {
				if (tileentity instanceof Container) {
					Containers.dropContents(worldIn, pos, (Container) tileentity);
					worldIn.updateNeighbourForOutputSignal(pos, this);
				}
				worldIn.removeBlockEntity(pos);
			}
		}
		impl.execute(OnReplacedBlockMethod.class).forEach(e -> e.onReplaced(state, worldIn, pos, newState, isMoving));
	}

	@Override
	public final BlockState rotate(BlockState state, Rotation rot) {
		return impl.one(MirrorRotateBlockMethod.class).map(e -> e.rotate(state, rot)).orElse(state);
	}

	@Override
	protected final void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		impl = TEMP.get();
		TEMP.set(null);
		impl.execute(CreateBlockStateBlockMethod.class).forEach(e -> e.createBlockStateDefinition(builder));
	}

	@Override
	public final void neighborChanged(BlockState state, Level world, BlockPos pos, Block nei_block, BlockPos nei_pos, boolean moving) {
		impl.execute(NeighborUpdateBlockMethod.class).forEach(e -> e.neighborChanged(this, state, world, pos, nei_block, nei_pos, moving));
		super.neighborChanged(state, world, pos, nei_block, nei_pos, moving);
	}

	@Override
	public final void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		impl.execute(RandomTickBlockMethod.class).forEach(e -> e.randomTick(state, world, pos, random));
	}

	@Override
	public final void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		impl.execute(ScheduleTickBlockMethod.class).forEach(e -> e.tick(state, world, pos, random));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random r) {
		impl.execute(AnimateTickBlockMethod.class).forEach(e -> e.animateTick(state, world, pos, r));
	}

	public static class BlockImplementor {

		private final Properties props;
		private final List<MultipleBlockMethod> list = new ArrayList<>();
		private final HashMap<Class<?>, SingletonBlockMethod> map = new HashMap<>();

		public BlockImplementor(Properties p) {
			props = p;
		}

		public BlockImplementor addImpls(BlockMethod... impls) {
			for (BlockMethod impl : impls) {
				if (impl instanceof MultipleBlockMethod)
					list.add((MultipleBlockMethod) impl);
				if (impl instanceof SingletonBlockMethod one) {
					List<Class<?>> list = new ArrayList<>();
					addOneImpl(one.getClass(), list);
					for (Class<?> cls : list) {
						if (map.containsKey(cls)) {
							throw new RuntimeException("class " + cls + " is implemented twice with " + map.get(cls) + " and " + impl);
						} else {
							map.put(cls, one);
						}
					}
				}
			}
			return this;
		}

		private void addOneImpl(Class<?> cls, List<Class<?>> list) {
			for (Class<?> ci : cls.getInterfaces()) {
				if (ci == SingletonBlockMethod.class) {
					throw new RuntimeException("class " + cls + " should not implement IOneImpl directly");
				}
				if (SingletonBlockMethod.class.isAssignableFrom(ci)) {
					Class<?>[] arr = ci.getInterfaces();
					if (arr.length == 1 && arr[0] == SingletonBlockMethod.class) {
						list.add(ci);
					} else {
						addOneImpl(ci, list);
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		public <T extends MultipleBlockMethod> Stream<T> execute(Class<T> cls) {
			return list.stream().filter(cls::isInstance).map(e -> (T) e);
		}

		@SuppressWarnings("unchecked")
		public <T extends SingletonBlockMethod> Optional<T> one(Class<T> cls) {
			return Optional.ofNullable((T) map.get(cls));
		}

	}

}
