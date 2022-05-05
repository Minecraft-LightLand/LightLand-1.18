package dev.xkmc.lightland.content.arcane.magic;

import dev.xkmc.lightland.content.arcane.internal.Arcane;
import dev.xkmc.lightland.content.arcane.internal.ArcaneType;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.init.registrate.LightlandVanillaMagic;
import dev.xkmc.l2library.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EarthAxe extends Arcane {

	private final float radius;
	private final int time;

	public EarthAxe(float radius, int time) {
		super(ArcaneType.PHECDA, 0);
		this.radius = radius;
		this.time = time;
	}

	@Override
	public boolean activate(Player player, LLPlayerData magic, ItemStack stack, LivingEntity target) {
		if (target == null)
			return false;
		Level w = player.level;
		strike(w, player, target);
		if (!w.isClientSide()) {
			search(w, player, radius, player.getPosition(1), target, false, this::strike);
			EffectUtil.addEffect(target, new MobEffectInstance(LightlandVanillaMagic.HEAVY.get(), time, 1),
					EffectUtil.AddReason.SKILL, player);
			EffectUtil.addEffect(target, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, 4),
					EffectUtil.AddReason.SKILL, player);
			double x = target.getX();
			double y = target.getY();
			double z = target.getZ();
			target.teleportTo(x, y - 1, z);
		}
		return true;
	}

	private void strike(Level w, Player player, LivingEntity target) {
		if (!w.isClientSide()) {
			EffectUtil.addEffect(target, new MobEffectInstance(LightlandVanillaMagic.HEAVY.get(), time, 0),
					EffectUtil.AddReason.SKILL, player);
			EffectUtil.addEffect(target, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, 2),
					EffectUtil.AddReason.SKILL, player);
		}
	}

}
