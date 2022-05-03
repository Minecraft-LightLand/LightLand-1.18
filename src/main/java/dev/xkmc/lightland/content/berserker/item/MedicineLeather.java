package dev.xkmc.lightland.content.berserker.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MedicineLeather extends Item implements MedicineItem {

	private final int rep;

	public MedicineLeather(int rep, Properties props) {
		super(props);
		this.rep = rep;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		for (ItemStack stack : player.getInventory().armor) {
			if (stack.getItem() instanceof MedicineArmor) {
				if (MedicineItem.eq(itemstack, stack) && stack.getDamageValue() >= rep / 2) {
					stack.setDamageValue(Math.max(0, stack.getDamageValue() - rep));
					level.playSound(null, player.getX(), player.getY(), player.getZ(),
							SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.NEUTRAL, 1, 1);
					if (!player.getAbilities().instabuild) {
						itemstack.shrink(1);
					}
					return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
				}
			}
		}
		return InteractionResultHolder.pass(itemstack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		PotionUtils.addPotionTooltip(stack, list, 1);
	}

}
