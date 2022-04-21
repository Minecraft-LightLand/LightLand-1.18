package dev.hikarishima.lightland.mixin;

import dev.hikarishima.lightland.content.archery.feature.bow.GlowTargetAimFeature;
import dev.hikarishima.lightland.util.RayTraceUtil;
import dev.lcy0x1.block.DelegateBlockImpl;
import dev.lcy0x1.block.mult.FallOnBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	private Vec3 position;

	@Shadow
	public Level level;

	@Inject(at = @At("HEAD"), method = "isCurrentlyGlowing", cancellable = true)
	public void isCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
		if (GlowTargetAimFeature.TARGET.target == (Object) this) {
			cir.setReturnValue(true);
		}
		if (RayTraceUtil.TARGET.target == (Object) this) {
			cir.setReturnValue(true);
		}
	}

	@Inject(at = @At("HEAD"), method = "getOnPos", cancellable = true)
	public void getOnPos(CallbackInfoReturnable<BlockPos> cir) {
		int i = Mth.floor(this.position.x);
		int j = Mth.floor(this.position.y);
		int k = Mth.floor(this.position.z);
		BlockPos blockpos = new BlockPos(i, j, k);
		BlockState state = level.getBlockState(blockpos);
		if (state.getBlock() instanceof DelegateBlockImpl block) {
			if (block.getImpl().execute(FallOnBlockMethod.class).findAny().isPresent())
				cir.setReturnValue(blockpos);
		}
	}

}
