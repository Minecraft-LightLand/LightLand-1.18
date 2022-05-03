package dev.xkmc.lightland.content.archery.item;

import dev.xkmc.lightland.content.archery.feature.FeatureList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.NonNullLazy;

public class GenericArrowItem extends ArrowItem {

	public record ArrowConfig(float damage, int punch, boolean is_inf, NonNullLazy<FeatureList> feature) {
	}

	public final ArrowConfig config;

	public GenericArrowItem(Properties properties, ArrowConfig config) {
		super(properties);
		this.config = config;
	}

	public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity user) {
		Arrow arrow = new Arrow(level, user);
		arrow.setEffectsFromItem(stack);
		return arrow;
	}

	public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
		int enchant = EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.INFINITY_ARROWS, bow);
		return enchant > 0 && config.is_inf();
	}

}
