package dev.hikarishima.lightland.content.common.effect.force;

import dev.hikarishima.lightland.content.common.effect.ForceEffect;
import dev.xkmc.l2library.util.MathHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class WaterTrapEffect extends MobEffect implements ForceEffect {

	public static final UUID ID = MathHelper.getUUIDfromString("lightland:water_trap");

	public WaterTrapEffect(MobEffectCategory type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.FLYING_SPEED, ID.toString(), -0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.JUMP_STRENGTH, ID.toString(), -0.4f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ID.toString(), -0.4f, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), ID.toString(), -0.3f, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

}
