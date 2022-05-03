package dev.xkmc.lightland.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.xkmc.lightland.content.archery.item.GenericBowItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

	@Shadow
	protected abstract void applyItemArmTransform(PoseStack pose, HumanoidArm arm, float f);

	@Shadow
	public abstract void renderItem(LivingEntity p_109323_, ItemStack p_109324_, ItemTransforms.TransformType p_109325_, boolean p_109326_, PoseStack p_109327_, MultiBufferSource p_109328_, int p_109329_);

	@Shadow
	protected abstract void applyItemArmAttackTransform(PoseStack p_109336_, HumanoidArm p_109337_, float p_109338_);

	@Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
	public void renderArmWithItem(AbstractClientPlayer player, float partial_tick, float f9,
								  InteractionHand hand, float f, ItemStack stack, float f2, PoseStack pose,
								  MultiBufferSource buffer, int i, CallbackInfo ci) {
		if (stack.getItem() instanceof GenericBowItem bow) {
			boolean is_right_hand = hand == InteractionHand.MAIN_HAND;
			HumanoidArm arm = is_right_hand ? player.getMainArm() : player.getMainArm().getOpposite();
			pose.pushPose();
			int k = is_right_hand ? 1 : -1;
			if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
				this.applyItemArmTransform(pose, arm, f2);
				pose.translate(k * -0.2785682F, 0.18344387F, 0.15731531F);
				pose.mulPose(Vector3f.XP.rotationDegrees(-13.935F));
				pose.mulPose(Vector3f.YP.rotationDegrees((float) k * 35.3F));
				pose.mulPose(Vector3f.ZP.rotationDegrees((float) k * -9.785F));
				float pull_time = (float) stack.getUseDuration() - ((float) player.getUseItemRemainingTicks() - partial_tick + 1);
				float pull = bow.getPowerForTime(player, pull_time);
				if (pull > 0.1F) {
					float f15 = Mth.sin((pull_time - 0.1F) * 1.3F);
					float f18 = pull - 0.1F;
					float f20 = f15 * f18;
					pose.translate(0, f20 * 0.004F, 0);
				}

				pose.translate(pull * 0.0F, 0, pull * 0.04F);
				pose.scale(1.0F, 1.0F, 1.0F + pull * 0.2F);
				pose.mulPose(Vector3f.YN.rotationDegrees((float) k * 45.0F));
			} else {
				float f5 = -0.4F * Mth.sin(Mth.sqrt(f) * (float) Math.PI);
				float f6 = 0.2F * Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F));
				float f10 = -0.2F * Mth.sin(f * (float) Math.PI);
				pose.translate((float) k * f5, f6, f10);
				this.applyItemArmTransform(pose, arm, f2);
				this.applyItemArmAttackTransform(pose, arm, f);
			}
			ItemTransforms.TransformType type = is_right_hand ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
			this.renderItem(player, stack, type, !is_right_hand, pose, buffer, i);
			pose.popPose();
			ci.cancel();
		}
	}

}
