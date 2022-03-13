package dev.hikarishima.lightland.util;

import dev.hikarishima.lightland.init.registrate.VanillaMagicRegistrate;
import dev.hikarishima.lightland.init.special.ArcaneRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;

public class LightLandFakeEntity {

	/**
	 * force add effect, make boss not override
	 * for icon use only, such as Arcane Mark on Wither and Ender Dragon
	 */
	public static void addEffect(LivingEntity e, MobEffectInstance ins, Entity source) {
		MobEffectInstance effectinstance = e.getActiveEffectsMap().get(ins.getEffect());
		MinecraftForge.EVENT_BUS.post(new PotionEvent.PotionAddedEvent(e, effectinstance, ins, source));
		if (effectinstance == null) {
			e.getActiveEffectsMap().put(ins.getEffect(), ins);
			e.onEffectAdded(ins, source);
		} else if (effectinstance.update(ins)) {
			e.onEffectUpdated(effectinstance, true, source);
		}
	}

	public static void addArcane(LivingEntity target, Entity source) {
		addEffect(target, new MobEffectInstance(VanillaMagicRegistrate.ARCANE.get(), ArcaneRegistry.ARCANE_TIME), source);
	}

}
