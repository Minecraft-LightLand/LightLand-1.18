package dev.xkmc.cuisine.content.tools.jar;

import dev.hikarishima.lightland.content.common.render.TileInfoOverlay;
import dev.hikarishima.lightland.init.data.AllTags;
import dev.lcy0x1.base.*;
import dev.lcy0x1.block.BlockContainer;
import dev.lcy0x1.serial.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class JarBlockEntity extends BaseBlockEntity implements TickableBlockEntity,
		BaseContainerListener<BaseContainer>, BlockContainer,
		TileInfoOverlay.TileInfoProvider {

	public class RecipeContainer extends BaseContainer {

		public RecipeContainer(int size) {
			super(size);
		}

		public JarBlockEntity getTile() {
			return JarBlockEntity.this;
		}

	}

	@SerialClass.SerialField(toClient = true)
	protected final BaseContainer inventory = new BaseContainer(3).add(this);
	@SerialClass.SerialField(toClient = true)
	protected final BaseTank fluids = new BaseTank(1, 1000)
			.setPredicate(e -> AllTags.AllFluidTags.PAN_ACCEPT.matches(e.getFluid())).add(this);

	@SerialClass.SerialField
	private int max_time, time;
	@SerialClass.SerialField
	private ResourceLocation recipe;

	public JarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void notifyTile(@Nullable BaseContainer cont) {
		this.setChanged();
		this.sync();
		this.resetProgress();
	}

	@Override
	public List<Container> getContainers() {
		return List.of(inventory);
	}

	@Override
	public void tick() {
		if (time > 0) {
			time--;
			if (time == 0) {
				if (level != null && recipe != null) {

				}
				notifyTile(null);
			}
		}
	}

	public void resetProgress() {
		// recipe
		max_time = 0;
		time = max_time;
	}

	@Override
	public List<TileInfoOverlay.IDrawable> getContents() {
		List<TileInfoOverlay.IDrawable> list = new ArrayList<>();
		for (ItemStack stack : inventory.getAsList()) {
			if (!stack.isEmpty())
				list.add(new TileInfoOverlay.ItemDrawable(stack));
		}
		for (FluidStack stack : fluids.getAsList()) {
			if (!stack.isEmpty())
				list.add(new TileInfoOverlay.FluidDrawable(stack, 1000));
		}
		return list;
	}
}
