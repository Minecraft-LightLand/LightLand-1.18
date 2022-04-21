package dev.hikarishima.lightland.util;

import dev.hikarishima.lightland.content.common.item.generic.GenericArmorItem;
import dev.hikarishima.lightland.init.registrate.LightlandVanillaMagic;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class DamageUtil {

	public static void dealDamage(LivingEntity target, DamageSource source, float damage) {
		if (source.isFire() && target.fireImmune())
			return;
		target.hurt(source, damage);
	}

	public static double getMagicReduced(LivingEntity target, DamageSource source, double damage) {
		if (source.isBypassMagic() || source.isBypassInvul())
			return damage;

		double level = 0;
		MobEffectInstance ins = target.getEffect(MobEffects.DAMAGE_RESISTANCE);
		if (ins != null) level += (ins.getAmplifier() + 1) * 20;
		level += EnchantmentHelper.getDamageProtection(target.getArmorSlots(), source) * 4;
		MobEffectInstance dis = target.getEffect(LightlandVanillaMagic.DISPELL.get());
		int dispell = dis == null ? 1 : Math.max(1, 2 << dis.getAmplifier());
		level /= dispell;
		if (source.isMagic()) {
			if (dis != null) {
				level += (dis.getAmplifier() + 1) * 70;
			}
			for (ItemStack stack : target.getArmorSlots()) {
				if (stack.getItem() instanceof GenericArmorItem armor) {
					level += armor.getConfig().getMagicImmune();
				}
			}
		}
		return damage * Math.exp(-0.01 * level);
	}

}
