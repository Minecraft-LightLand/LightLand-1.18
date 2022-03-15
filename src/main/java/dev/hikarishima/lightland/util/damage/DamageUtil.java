package dev.hikarishima.lightland.util.damage;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
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
		int level = 0;
		MobEffectInstance ins = target.getEffect(MobEffects.DAMAGE_RESISTANCE);
		if (ins != null) level += (ins.getAmplifier() + 1) * 20;
		level += EnchantmentHelper.getDamageProtection(target.getArmorSlots(), source) * 4;
		return damage * Math.exp(-0.01 * level);
	}

}
