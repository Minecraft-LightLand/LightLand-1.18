package dev.xkmc.cuisine.content.tools.basin;

import dev.lcy0x1.base.BaseBlockEntity;
import dev.lcy0x1.base.BaseContainer;
import dev.lcy0x1.base.BaseContainerListener;
import dev.lcy0x1.base.BaseTank;
import dev.lcy0x1.serial.SerialClass;
import dev.xkmc.cuisine.init.data.CuisineTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class BasinBlockEntity extends BaseBlockEntity implements BaseContainerListener<BaseContainer> {

	public class RecipeContainer extends BaseContainer {

		public RecipeContainer(int size) {
			super(size);
		}

		public BasinBlockEntity getTile() {
			return BasinBlockEntity.this;
		}

	}

	public static final int MAX_FLUID = 500;

	@SerialClass.SerialField(toClient = true)
	protected final RecipeContainer inventory = (RecipeContainer) new RecipeContainer(8)
			.setPredicate(stack -> this.inventory.countSpace() > 4).add(this);

	@SerialClass.SerialField(toClient = true)
	protected final BaseTank fluids = new BaseTank(1, MAX_FLUID)
			.setPredicate(e -> CuisineTags.AllFluidTags.JAR_ACCEPT.matches(e.getFluid())).add(this);

	protected final LazyOptional<IItemHandlerModifiable> itemCapability;
	protected final LazyOptional<IFluidHandler> fluidCapability;

	public BasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemCapability = LazyOptional.of(() -> new InvWrapper(inventory));
		fluidCapability = LazyOptional.of(() -> fluids);
	}

	@Override
	public void notifyTile(@Nullable BaseContainer cont) {
		this.setChanged();
		this.sync();
	}


}
