package dev.hikarishima.lightland.mixin;

import dev.hikarishima.lightland.content.archery.feature.bow.EnderShootFeature;
import dev.hikarishima.lightland.content.archery.feature.bow.GlowTargetAimFeature;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(at = @At("HEAD"), method = "isCurrentlyGlowing", cancellable = true)
    public void isCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (GlowTargetAimFeature.TARGET.target == (Object) this) {
            cir.setReturnValue(true);
        }
        if (EnderShootFeature.TARGET.target == (Object) this) {
            cir.setReturnValue(true);
        }
    }

}
