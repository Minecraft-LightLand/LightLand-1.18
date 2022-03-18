package dev.hikarishima.lightland.content.questline.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SlimeCarpet extends CarpetBlock {

	public SlimeCarpet(Properties prop) {
		super(prop);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity.isOnGround() && entity.getY() - pos.getY() < 1 / 16f)
			entity.makeStuckInBlock(state, new Vec3(0.25D, (double) 0.05F, 0.25D));
	}
}
