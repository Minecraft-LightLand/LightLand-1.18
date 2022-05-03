package dev.xkmc.cuisine.content.tools.base.methods;

import dev.xkmc.l2library.block.mult.AnimateTickBlockMethod;
import dev.xkmc.cuisine.content.tools.base.CuisineUtil;
import dev.xkmc.cuisine.content.tools.base.tile.TimeTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class TimeProcessing implements AnimateTickBlockMethod {

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random r) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TimeTile jar) {
			if (jar.processing() && r.nextFloat() > 0.8) {
				CuisineUtil.spawnParticle(world, pos, r);
			}
		}
	}

}
