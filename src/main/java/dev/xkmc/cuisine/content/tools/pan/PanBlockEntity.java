package dev.xkmc.cuisine.content.tools.pan;

import dev.lcy0x1.base.*;
import dev.lcy0x1.block.BlockContainer;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.content.tools.base.TileInfoOverlay;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SerialClass
public class PanBlockEntity extends BaseBlockEntity implements TickableBlockEntity, IAnimatable, BlockContainer,
		BaseContainerListener<BaseContainer>, TileInfoOverlay.TileInfoProvider {

	public class RecipeContainer extends BaseContainer {

		public RecipeContainer(int size) {
			super(size);
		}

		public PanBlockEntity getTile() {
			return PanBlockEntity.this;
		}

	}

	public static final int MAX_FLUID = 8 * 50;

	private final AnimationFactory manager = new AnimationFactory(this);

	@SerialClass.SerialField(toClient = true)
	protected final RecipeContainer inputInventory = (RecipeContainer) new RecipeContainer(8).setMax(1)
			.setPredicate(e -> canAccess() && CuisineTags.AllItemTags.CAN_COOK.matches(e)).add(this);

	@SerialClass.SerialField(toClient = true)
	protected final BaseTank fluids = new BaseTank(4, MAX_FLUID).setClickMax(50)
			.setPredicate(e -> canAccess() && CuisineTags.AllFluidTags.JAR_ACCEPT.matches(e.getFluid()))
			.setExtract(() -> false).add(this);

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;
	protected final LazyOptional<IFluidHandler> fluidCapability;

	private BlockState prevState = null;

	@SerialClass.SerialField(toClient = true)
	public int cooking_max = 0, cooking = 0;
	@SerialClass.SerialField(toClient = true)
	public ItemStack result = ItemStack.EMPTY;

	public PanBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemCapability = LazyOptional.of(() -> new InvWrapper(inputInventory));
		fluidCapability = LazyOptional.of(() -> fluids);
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
	@Nonnull
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemCapability.cast();
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return fluidCapability.cast();
		return super.getCapability(cap, side);
	}

	@Override
	public List<Container> getContainers() {
		return List.of(inputInventory);
	}

	@Override
	public void notifyTile(@Nullable BaseContainer cont) {
		this.setChanged();
		this.sync();
	}

	private boolean canAccess() {
		return !getBlockState().getValue(BlockStateProperties.SIGNAL_FIRE);
	}

	public void stopCooking() {
		if (cooking_max > 0) {
			if (cooking > 0) {
				result = ItemStack.EMPTY;
			}
			cooking = 0;
			cooking_max = 0;
		}
		notifyTile(null);
	}

	public void dumpInventory() {
		if (level == null) return;
		if (!inputInventory.isEmpty()) {
			Containers.dropContents(level, this.getBlockPos().above(), inputInventory);
		} else
			fluids.clear();
		notifyTile(null);
	}

	public boolean startCooking() {
		if (level == null) return false;
		boolean ans = result.isEmpty() && !inputInventory.isEmpty() || !fluids.isEmpty();
		if (!ans) return false;
		Optional<PanRecipe> r = level.getRecipeManager().getRecipeFor(CuisineRecipe.RT_PAN, inputInventory, level);
		inputInventory.clear();
		fluids.clear();
		if (r.isEmpty()) {
			cooking_max = 100;
			cooking = cooking_max;
			result = ItemStack.EMPTY;
		} else {
			PanRecipe recipe = r.get();
			cooking_max = recipe.time;
			cooking = cooking_max;
			result = recipe.result.copy();
		}
		notifyTile(null);
		return true;
	}

	@Override
	public void tick() {
		if (cooking > 0) {
			cooking--;
			if (level != null && cooking == 0 && !level.isClientSide()) {
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.SIGNAL_FIRE, false));
				stopCooking();
			}
		}
	}

	@Override
	public List<TileInfoOverlay.IDrawable> getContents() {
		List<TileInfoOverlay.IDrawable> list = new ArrayList<>();
		for (ItemStack stack : inputInventory.getAsList()) {
			if (!stack.isEmpty())
				list.add(new TileInfoOverlay.ItemDrawable(stack));
		}
		for (FluidStack stack : fluids.getAsList()) {
			if (!stack.isEmpty())
				list.add(new TileInfoOverlay.FluidDrawable(stack, MAX_FLUID, 50));
		}
		return list;
	}
}
