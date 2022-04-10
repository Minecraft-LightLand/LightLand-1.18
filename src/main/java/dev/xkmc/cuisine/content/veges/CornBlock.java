package dev.xkmc.cuisine.content.veges;

import dev.xkmc.cuisine.init.data.CuisineCropType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

public class CornBlock extends DoubleCropBlock {

	public CornBlock(CuisineCropType type, Properties props) {
		super(type, props);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext col) {
		if (!isLower(state)) {
			BlockState lower = level.getBlockState(pos.below());
			if (isLower(lower))
				return CuisineCropBlock.SHAPE_BY_AGE[lower.getValue(getAgeProperty())];
		}
		return Shapes.block();
	}


	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
	}

	@Override
	protected boolean hasUpper(BlockState state) {
		return getAge(state) > 0;
	}

	@Override
	public void growCrops(Level level, BlockPos pos, BlockState state) {
		if (getAge(state) < 1 && !level.getBlockState(pos.above()).isAir())
			return;
		super.growCrops(level, pos, state);
		if (getAge(state) >= 0) {
			level.setBlock(pos.above(), defaultBlockState().setValue(getAgeProperty(), 8), 3);
		}
	}

	@SubscribeEvent
	public static void onCropsGrowPre(BlockEvent.CropGrowEvent.Pre event) {
		if (event.getState().getBlock() instanceof CornBlock block && block.getAge(event.getState()) == 0)
			if (!event.getWorld().getBlockState(event.getPos().above()).isAir())
				event.setResult(Event.Result.DENY);
	}

	@SubscribeEvent
	public static void onCropsGrowPost(BlockEvent.CropGrowEvent.Post event) {
		BlockState state = event.getState();
		if (state.getBlock() instanceof CornBlock block && block.getAge(state) > 0) {
			event.getWorld().setBlock(event.getPos().above(), block.defaultBlockState().setValue(block.getAgeProperty(), 8), 3);
		}
	}

}