package dev.hikarishima.lightland.content.archery.feature.types;

import dev.hikarishima.lightland.content.archery.entity.GenericArrowEntity;
import dev.hikarishima.lightland.content.archery.feature.BowArrowFeature;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;

public interface OnHitFeature extends BowArrowFeature {

    void onHitEntity(GenericArrowEntity genericArrow, LivingEntity target);

    void onHitBlock(GenericArrowEntity genericArrow, BlockHitResult result);

}
