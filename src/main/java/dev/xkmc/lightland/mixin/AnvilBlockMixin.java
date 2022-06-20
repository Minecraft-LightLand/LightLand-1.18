package dev.xkmc.lightland.mixin;

import dev.xkmc.lightland.init.registrate.LightlandBlocks;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private static void injectDamage(BlockState state, CallbackInfoReturnable<BlockState> info) {
		if (state.getBlock() == LightlandBlocks.ETERNAL_ANVIL.get()) {
			info.setReturnValue(state);
			info.cancel();
		}
	}

}
