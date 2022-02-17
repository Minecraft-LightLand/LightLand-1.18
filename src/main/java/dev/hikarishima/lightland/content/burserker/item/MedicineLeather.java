package dev.hikarishima.lightland.content.burserker.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MedicineLeather extends Item implements MedicineItem {

    public MedicineLeather(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        for (ItemStack stack : player.getInventory().armor) {
            if (stack.getItem() instanceof MedicineArmor) {
                if (MedicineItem.eq(itemstack, stack) && stack.isDamaged()) {
                    stack.setDamageValue(Math.min(0, stack.getDamageValue() - 50));
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

}
