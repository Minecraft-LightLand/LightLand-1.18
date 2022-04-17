package dev.lcy0x1.block.mult;

import dev.lcy0x1.block.type.MultipleBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface FallOnBlockMethod extends MultipleBlockMethod {

	boolean fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float height);

}
