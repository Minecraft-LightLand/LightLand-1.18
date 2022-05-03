package dev.xkmc.cuisine.content.tools.firepit.methods;

import dev.xkmc.l2library.block.one.EntityInsideBlockMethod;
import dev.xkmc.cuisine.util.DamageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FirePitBurnMethod implements EntityInsideBlockMethod {

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity le && state.getValue(BlockStateProperties.LIT)) {
			DamageUtil.dealDamage(le, DamageSource.IN_FIRE, 1);
		}
	}

}
