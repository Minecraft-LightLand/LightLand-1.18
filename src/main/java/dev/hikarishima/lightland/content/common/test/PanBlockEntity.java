package dev.hikarishima.lightland.content.common.test;

import dev.lcy0x1.base.BaseBlockEntity;
import dev.lcy0x1.base.BaseContainer;
import dev.lcy0x1.base.BaseContainerListener;
import dev.lcy0x1.base.BaseTank;
import dev.lcy0x1.block.BlockContainer;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;

@SerialClass
public class PanBlockEntity extends BaseBlockEntity implements IAnimatable, BlockContainer, BaseContainerListener<BaseContainer> {

	private final AnimationFactory manager = new AnimationFactory(this);

	@SerialClass.SerialField
	protected final BaseContainer inputInventory = new BaseContainer(9).setMax(1).add(this);
	@SerialClass.SerialField
	protected final BaseContainer outputInventory = new BaseContainer(1).setPredicate(e -> false).add(this);
	@SerialClass.SerialField
	protected final BaseTank fluids = new BaseTank(9, 250);

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;
	protected final LazyOptional<IFluidHandler> fluidCapability;

	public PanBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(new InvWrapper(inputInventory), new InvWrapper(outputInventory)));
		fluidCapability = LazyOptional.of(() -> fluids);
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "main", 0, e -> {
			BlockState state = e.getAnimatable().getBlockState();
			boolean lit = state.getValue(BlockStateProperties.LIT);
			boolean open = state.getValue(BlockStateProperties.OPEN);
			boolean cooking = state.getValue(BlockStateProperties.SIGNAL_FIRE);
			if (cooking) {
				e.getController().setAnimation(new AnimationBuilder().addAnimation("ing", true));
			} else if (open) {
				e.getController().setAnimation(new AnimationBuilder().addAnimation("open_normal", true));
			} else {
				e.getController().setAnimation(new AnimationBuilder().addAnimation("close_normal", true));
			}
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimationFactory getFactory() {
		return manager;
	}

	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemCapability.cast();
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return fluidCapability.cast();
		return super.getCapability(cap, side);
	}

	@Override
	public List<Container> getContainers() {
		return List.of(inputInventory, outputInventory);
	}

	@Override
	public void notifyTile(BaseContainer cont) {
		this.setChanged();
		this.sync();
	}

}
