package dev.lcy0x1.block.mult;

import dev.lcy0x1.block.type.MultipleBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface SetPlacedByBlockMethod extends MultipleBlockMethod {

	void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack);

}
