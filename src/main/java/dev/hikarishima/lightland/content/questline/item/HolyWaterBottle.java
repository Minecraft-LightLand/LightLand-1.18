package dev.hikarishima.lightland.content.questline.item;


import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class HolyWaterBottle extends Item {

	private static final int DRINK_DURATION = 10;

	public HolyWaterBottle(Item.Properties p_41346_) {
		super(p_41346_);
	}

	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
		super.finishUsingItem(stack, level, user);
		if (!level.isClientSide()) {
			user.removeAllEffects();
		}
		if (stack.isEmpty()) {
			return new ItemStack(Items.GLASS_BOTTLE);
		} else {
			if (user instanceof Player player && !((Player) user).getAbilities().instabuild) {
				ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
				if (!player.getInventory().add(itemstack)) {
					player.drop(itemstack, false);
				}
			}
			return stack;
		}
	}

	public int getUseDuration(ItemStack stack) {
		return DRINK_DURATION;
	}

	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	public SoundEvent getDrinkingSound() {
		return SoundEvents.GENERIC_DRINK;
	}

	public SoundEvent getEatingSound() {
		return SoundEvents.GENERIC_DRINK;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(level, player, hand);
	}
}