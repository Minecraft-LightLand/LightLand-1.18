package dev.hikarishima.lightland.util;

import dev.hikarishima.lightland.content.common.effect.ForceEffect;
import dev.hikarishima.lightland.init.registrate.LightlandVanillaMagic;
import dev.hikarishima.lightland.init.special.ArcaneRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;

public class EffectAddUtil {

	public enum AddReason {
		NONE, PROF, FORCE, SKILL, SELF
	}

	private static final ThreadLocal<AddReason> REASON = new ThreadLocal<>();

	/**
	 * force add effect, make boss not override
	 * for icon use only, such as Arcane Mark on Wither and Ender Dragon
	 */
	private static void forceAddEffect(LivingEntity e, MobEffectInstance ins, Entity source) {
		MobEffectInstance effectinstance = e.getActiveEffectsMap().get(ins.getEffect());
		MinecraftForge.EVENT_BUS.post(new PotionEvent.PotionAddedEvent(e, effectinstance, ins, source));
		if (effectinstance == null) {
			e.getActiveEffectsMap().put(ins.getEffect(), ins);
			e.onEffectAdded(ins, source);
		} else if (effectinstance.update(ins)) {
			e.onEffectUpdated(effectinstance, true, source);
		}
	}

	public static void addEffect(LivingEntity entity, MobEffectInstance ins, AddReason reason, Entity source) {
		if (entity == source)
			reason = AddReason.SELF;
		if (ins.getEffect() instanceof ForceEffect)
			reason = AddReason.FORCE;
		ins = new MobEffectInstance(ins.getEffect(), ins.getDuration(), ins.getAmplifier(),
				ins.isAmbient(), reason != AddReason.FORCE && ins.isVisible(), ins.showIcon());
		REASON.set(reason);
		if (ins.getEffect() instanceof ForceEffect)
			forceAddEffect(entity, ins, source);
		else if (ins.getEffect().isInstantenous())
			ins.getEffect().applyInstantenousEffect(null, null, entity, ins.getAmplifier(), 1);
		else entity.addEffect(ins, source);
		REASON.set(AddReason.NONE);
	}

	public static void refreshEffect(LivingEntity entity, MobEffectInstance ins, AddReason reason, Entity source) {
		MobEffectInstance cur = entity.getEffect(ins.getEffect());
		if (cur == null || cur.getAmplifier() < ins.getAmplifier())
			addEffect(entity, ins, reason, source);
	}

	public static void addArcane(LivingEntity target, Entity source) {
		addEffect(target, new MobEffectInstance(LightlandVanillaMagic.ARCANE.get(), ArcaneRegistry.ARCANE_TIME), AddReason.SKILL, source);
	}

	public static AddReason getReason() {
		AddReason ans = REASON.get();
		return ans == null ? AddReason.NONE : ans;
	}

}
