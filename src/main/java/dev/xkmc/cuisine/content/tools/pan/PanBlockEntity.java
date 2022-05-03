package dev.xkmc.cuisine.content.tools.pan;

import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.base.handlers.ContainerResultHandler;
import dev.xkmc.cuisine.content.tools.base.handlers.CookHandler;
import dev.xkmc.cuisine.content.tools.base.tile.CookTile;
import dev.xkmc.cuisine.content.tools.base.tile.CuisineTankTile;
import dev.xkmc.cuisine.content.tools.base.tile.LidTile;
import dev.xkmc.cuisine.content.tools.base.tile.LitTile;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@SerialClass
public class PanBlockEntity extends CuisineTankTile<PanBlockEntity> implements TickableBlockEntity, IAnimatable,
		LitTile, CookTile, LidTile {

	public static final int MAX_FLUID = 8 * 50;

	private final AnimationFactory manager = new AnimationFactory(this);
	private BlockState prevState = null;

	@SerialClass.SerialField(toClient = true)
	private final ContainerResultHandler<PanBlockEntity> resultHandler = new ContainerResultHandler<>(this);
	@SerialClass.SerialField(toClient = true)
	private final CookHandler<PanBlockEntity, PanRecipe> cookHandler =
			new CookHandler<>(this, CuisineRecipes.RT_PAN.get(), resultHandler, true);

	public PanBlockEntity(BlockEntityType<PanBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state, t -> new RecipeContainer<>(t, 8).setMax(1)
						.setPredicate(e -> t.canAccess() && CuisineTags.AllItemTags.CAN_COOK.matches(e)).add(t),
				new FluidInfo(4, MAX_FLUID, 50));
		fluids.setClickMax(50)
				.setPredicate(e -> canAccess() && CuisineTags.AllFluidTags.JAR_ACCEPT.matches(e.getFluid()))
				.setExtract(() -> false);
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "main", 0, e -> {
			BlockState state = e.getAnimatable().getBlockState();
			boolean shouldUpdate = prevState != state;
			if (prevState == null) prevState = state;
			boolean lit = state.getValue(BlockStateProperties.LIT);
			boolean open = state.getValue(BlockStateProperties.OPEN);
			boolean cooking = state.getValue(BlockStateProperties.SIGNAL_FIRE);
			boolean prev_open = prevState.getValue(BlockStateProperties.OPEN);
			if (shouldUpdate) {
				if (cooking) {
					e.getController().setAnimation(new AnimationBuilder().addAnimation("ing", true));
				} else if (open) {
					if (!prev_open) {
						e.getController().setAnimation(new AnimationBuilder().addAnimation("open_ing", false)
								.addAnimation("open_normal", true));
					} else {
						e.getController().setAnimation(new AnimationBuilder().addAnimation("open_normal", true));
					}
				} else {
					if (prev_open) {
						e.getController().setAnimation(new AnimationBuilder().addAnimation("close_ing", false)
								.addAnimation("close_normal", true));
					} else {

						e.getController().setAnimation(new AnimationBuilder().addAnimation("close_normal", true));
					}
				}
			}
			prevState = state;
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimationFactory getFactory() {
		return manager;
	}

	@Override
	public void notifyTile() {
		this.setChanged();
		this.sync();
	}

	private boolean canAccess() {
		return !getBlockState().getValue(BlockStateProperties.SIGNAL_FIRE);
	}

	public void dumpInventory() {
		if (!getBlockState().getValue(BlockStateProperties.OPEN)) return;
		if (!getResult().isEmpty()) {
			clearResult();
			notifyTile();
		} else {
			super.dumpInventory();
		}
	}

	@Override
	public void tick() {
		cookHandler.tick();
	}

	@Override
	public ItemStack getResult() {
		return resultHandler.result;
	}

	public void clearResult() {
		resultHandler.result = ItemStack.EMPTY;
	}

	@Override
	public boolean canTake() {
		return getBlockState().getValue(BlockStateProperties.OPEN);
	}

	@Override
	public void onLit(Level level, BlockPos pos, BlockState next) {
		boolean open = next.getValue(BlockStateProperties.OPEN);
		if (!level.isClientSide()) {
			if (!open) {
				if (cookHandler.startCooking()) {
					next = next.setValue(BlockStateProperties.SIGNAL_FIRE, true)
							.setValue(BlockStateProperties.OPEN, false);
				}
			}
			level.setBlockAndUpdate(pos, next);
		}
	}

	@Override
	public void onUnlit(Level level, BlockPos pos, BlockState state) {
		state = state.setValue(BlockStateProperties.SIGNAL_FIRE, false);
		if (!level.isClientSide()) {
			cookHandler.stopCooking();
			level.setBlockAndUpdate(pos, state);
		}
	}

	@Override
	public void onStopCooking() {
		if (level == null) return;
		level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.SIGNAL_FIRE, false));
	}

	@Override
	public boolean canChangeLid() {
		return getResult().isEmpty();
	}

	@Override
	public BlockState onLidChange(BlockState state, boolean open) {
		state = state.setValue(BlockStateProperties.OPEN, !open);
		boolean lit = state.getValue(BlockStateProperties.LIT);
		boolean cooking = state.getValue(BlockStateProperties.SIGNAL_FIRE);
		if (!open && cooking) {
			state = state.setValue(BlockStateProperties.SIGNAL_FIRE, false);
			cookHandler.stopCooking();
		}
		if (open && lit) {
			if (cookHandler.startCooking())
				state = state.setValue(BlockStateProperties.SIGNAL_FIRE, true)
						.setValue(BlockStateProperties.OPEN, false);
		}
		return state;
	}

}
