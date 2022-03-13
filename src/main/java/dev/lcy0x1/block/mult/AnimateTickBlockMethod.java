package dev.lcy0x1.block.mult;

import dev.lcy0x1.block.type.MultipleBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public interface AnimateTickBlockMethod extends MultipleBlockMethod {
	@OnlyIn(Dist.CLIENT)
	void animateTick(BlockState state, Level world, BlockPos pos, Random r);
}
