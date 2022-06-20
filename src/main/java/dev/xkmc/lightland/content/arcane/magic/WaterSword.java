package dev.xkmc.lightland.content.arcane.magic;

import dev.xkmc.l2library.effects.EffectUtil;
import dev.xkmc.lightland.content.arcane.internal.Arcane;
import dev.xkmc.lightland.content.arcane.internal.ArcaneType;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.init.registrate.LightlandVanillaMagic;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WaterSword extends Arcane {

	private final float radius;
	private final int time;

	public WaterSword(float radius, int time) {
		super(ArcaneType.MIZAR, 0);
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
			search(w, player, radius, target.getPosition(1), target, false, this::strike);
			EffectUtil.addEffect(target, new MobEffectInstance(LightlandVanillaMagic.WATER_TRAP.get(), time, 1),
					EffectUtil.AddReason.SKILL, player);
		}
		return true;
	}

	private void strike(Level w, Player player, LivingEntity target) {
		if (!w.isClientSide()) {
			EffectUtil.addEffect(target, new MobEffectInstance(LightlandVanillaMagic.WATER_TRAP.get(), time, 0),
					EffectUtil.AddReason.SKILL, player);
		}
	}

}
