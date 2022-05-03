package dev.xkmc.cuisine.content.misc;

import dev.xkmc.l2library.util.MathHelper;
import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.util.DamageUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class TasteEffect extends MobEffect {

	public static class Attribs {

		private final Attribute attrib;
		private final AttributeModifier.Operation op;
		private final float amount;

		public Attribs(Attribute attrib, AttributeModifier.Operation op, float amount) {
			this.attrib = attrib;
			this.op = op;
			this.amount = amount;
		}

	}

	public record Config(MobEffectCategory category, int color, int period, int damage, Attribs... attribs) {
	}

	public static final DamageSource SOURCE = new DamageSource("food_poison");

	private final Config config;

	public TasteEffect(Config config, String name) {
		super(config.category, config.color);
		this.config = config;
		UUID id = MathHelper.getUUIDfromString(Cuisine.MODID + ":" + name);
		for (Attribs a : config.attribs) {
			addAttributeModifier(a.attrib, id.toString(), a.amount, a.op);
		}
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int lv) {
		if (config.damage > 0) DamageUtil.dealDamage(entity, SOURCE, config.damage);
		if (config.damage < 0) entity.heal(-config.damage);
	}

	@Override
	public boolean isDurationEffectTick(int tick, int lv) {
		return config.period > 0 && tick % config.period == 0;
	}

}
