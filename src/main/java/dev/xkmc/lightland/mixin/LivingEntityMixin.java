package dev.xkmc.lightland.mixin;

import dev.xkmc.lightland.util.DamageUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Inject(at = @At("HEAD"), method = "getDamageAfterMagicAbsorb", cancellable = true)
	protected void getDamageAfterMagicAbsorb(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue((float) DamageUtil.getMagicReduced((LivingEntity) (Object) this, source, damage));
	}

}
