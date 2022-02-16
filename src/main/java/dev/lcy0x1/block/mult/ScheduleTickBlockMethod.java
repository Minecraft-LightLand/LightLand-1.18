package dev.lcy0x1.block.mult;

import dev.lcy0x1.block.type.MultipleBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public interface ScheduleTickBlockMethod extends MultipleBlockMethod {

    void tick(BlockState state, ServerLevel world, BlockPos pos, Random random);

}