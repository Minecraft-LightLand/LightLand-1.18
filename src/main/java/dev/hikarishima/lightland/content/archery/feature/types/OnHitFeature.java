package dev.hikarishima.lightland.content.archery.feature.types;

import dev.hikarishima.lightland.content.archery.feature.BowArrowFeature;
import dev.hikarishima.lightland.content.common.entity.GenericArrowEntity;
import dev.xkmc.l2library.util.ServerOnly;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;

public interface OnHitFeature extends BowArrowFeature {

	@ServerOnly
	void onHitEntity(GenericArrowEntity genericArrow, LivingEntity target);

	@ServerOnly
	void onHitBlock(GenericArrowEntity genericArrow, BlockHitResult result);

}
