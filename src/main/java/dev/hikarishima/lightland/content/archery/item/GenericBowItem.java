package dev.hikarishima.lightland.content.archery.item;

import dev.hikarishima.lightland.content.archery.controller.ArrowFeatureController;
import dev.hikarishima.lightland.content.archery.controller.BowFeatureController;
import dev.hikarishima.lightland.util.GenericItemStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.function.Predicate;

public class GenericBowItem extends BowItem {

    public record BowConfig(){

    }

    public final BowConfig config;

    public GenericBowItem(Properties properties, BowConfig config) {
        super(properties);
        this.config = config;
    }

    /**
     * on release bow
     */
    public void releaseUsing(ItemStack bow, Level level, LivingEntity user, int remaining_pull_time) {
        if (user instanceof Player player) {
            BowFeatureController.stopUsing(new GenericItemStack<GenericBowItem>(this, bow));
            boolean has_inf = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
            ItemStack arrow = player.getProjectile(bow);
            int pull_time = this.getUseDuration(bow) - remaining_pull_time;
            pull_time = ForgeEventFactory.onArrowLoose(bow, level, player, pull_time, !arrow.isEmpty() || has_inf);
            if (pull_time < 0) return;
            if (arrow.isEmpty() && !has_inf) {
                return;
            }
            if (arrow.isEmpty()) { // no arrow: use default arrow
                arrow = new ItemStack(Items.ARROW);
            }
            float power = getPowerForTime(pull_time);
            if (((double) power < 0.1D)) { // not enough power: cancel
                return;
            }
            boolean no_consume = player.getAbilities().instabuild || (arrow.getItem() instanceof ArrowItem && ((ArrowItem) arrow.getItem()).isInfinite(arrow, bow, player));
            if (!level.isClientSide) {
                shootArrowOnServer(player, level, bow, arrow, power, no_consume);
            }

            float pitch = 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F;
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, pitch);
            if (!no_consume && !player.getAbilities().instabuild) {
                arrow.shrink(1);
                if (arrow.isEmpty()) {
                    player.getInventory().removeItem(arrow);
                }
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    /**
     * create arrow entity and add to world
     */
    private void shootArrowOnServer(Player player, Level level, ItemStack bow, ItemStack arrow, float power, boolean no_consume) {
        ArrowItem arrowitem = (ArrowItem) (arrow.getItem() instanceof ArrowItem ? arrow.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(level, arrow, player);
        abstractarrow = customArrow(abstractarrow);
        abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 3.0F, 1.0F);
        if (power == 1.0F) {
            abstractarrow.setCritArrow(true);
        }

        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bow);
        if (j > 0) {
            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bow);
        if (k > 0) {
            abstractarrow.setKnockback(k);
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) > 0) {
            abstractarrow.setSecondsOnFire(100);
        }

        bow.hurtAndBreak(1, player, (p_40665_) -> {
            p_40665_.broadcastBreakEvent(player.getUsedItemHand());
        });
        if (no_consume || player.getAbilities().instabuild && (arrow.is(Items.SPECTRAL_ARROW) || arrow.is(Items.TIPPED_ARROW))) {
            abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        level.addFreshEntity(abstractarrow);
    }

    /**
     * power of arrow, range 0~1
     * Formula: (t*(t+2))/3
     * Full in 1 second
     */
    public static float getPowerForTime(int time) {
        float f = (float) time / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    public int getUseDuration(ItemStack stack) {
        return BowFeatureController.getMaxPullTime(new GenericItemStack<>(this, stack));
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    /**
     * On start pulling
     */
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean flag = !player.getProjectile(itemstack).isEmpty();

        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onArrowNock(itemstack, level, player, hand, flag);
        if (ret != null) return ret;

        if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            BowFeatureController.startUsing(new GenericItemStack<>(this, itemstack));
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack) -> {
            if (stack.is(ItemTags.ARROWS)) {
                return true;
            }
            if (stack.getItem() instanceof GenericArrowItem arrow) {
                return ArrowFeatureController.canBowUseArrow(this, new GenericItemStack<>(arrow, stack));
            }
            return false;
        };
    }

    /**
     * return custom arrow entity
     */
    public AbstractArrow customArrow(AbstractArrow arrow) {
        return arrow;
    }

    /**
     * For mobs
     */
    public int getDefaultProjectileRange() {
        return 15;
    }

}
