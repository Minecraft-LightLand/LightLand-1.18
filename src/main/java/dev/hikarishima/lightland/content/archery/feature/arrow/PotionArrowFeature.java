package dev.hikarishima.lightland.content.archery.feature.arrow;

import dev.hikarishima.lightland.content.archery.feature.types.OnHitFeature;
import dev.hikarishima.lightland.content.common.entity.GenericArrowEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;

public record PotionArrowFeature(MobEffectInstance... instances) implements OnHitFeature {

    @Override
    public void onHitEntity(GenericArrowEntity arrow, LivingEntity target) {
        for (MobEffectInstance instance : instances) {
            target.addEffect(new MobEffectInstance(instance));
        }
    }

    @Override
    public void onHitBlock(GenericArrowEntity arrow, BlockHitResult result) {
    }
}
