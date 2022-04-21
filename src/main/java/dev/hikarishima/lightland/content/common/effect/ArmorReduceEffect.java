package dev.hikarishima.lightland.content.common.effect;

import dev.lcy0x1.util.MathHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class ArmorReduceEffect extends MobEffect {

	public static final UUID ID = MathHelper.getUUIDfromString("lightland:armor_reduce");

	public ArmorReduceEffect(MobEffectCategory category, int color) {
		super(category, color);
		this.addAttributeModifier(Attributes.ARMOR, ID.toString(), -8f, AttributeModifier.Operation.ADDITION);
	}

}
