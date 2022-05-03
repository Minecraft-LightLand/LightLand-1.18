package dev.hikarishima.lightland.content.skill;

import dev.hikarishima.lightland.compat.TeamAccessor;
import dev.hikarishima.lightland.content.skill.internal.Skill;
import dev.hikarishima.lightland.content.skill.internal.SkillConfig;
import dev.hikarishima.lightland.content.skill.internal.SkillData;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.util.EffectAddUtil;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectSkill extends Skill<EffectSkill.Config, SkillData> {

	@SerialClass
	public static class Effect {

		@SerialClass.SerialField
		public ResourceLocation id;

		@SerialClass.SerialField
		public int duration;

		@SerialClass.SerialField
		public int amplifier;

	}

	@SerialClass
	public static class RangeEffect {

		@SerialClass.SerialField
		public ResourceLocation id;

		@SerialClass.SerialField
		public int duration;

		@SerialClass.SerialField
		public int amplifier;

		@SerialClass.SerialField
		public double range;

		@SerialClass.SerialField
		public boolean for_enemy;

	}

	@SerialClass
	public static class Config extends SkillConfig<SkillData> {

		@SerialClass.SerialField
		public Effect[][] effects;
		@SerialClass.SerialField
		public RangeEffect[][] range_effects;

		@SuppressWarnings("ConstantConditions")
		public void activate(ServerPlayer player, SkillData data) {
			int lv = Math.min(data.level, effects.length - 1);
			if (effects != null)
				for (Effect e : effects[lv]) {
					MobEffectInstance ins = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(e.id), e.duration, e.amplifier);
					EffectAddUtil.addEffect(player, ins, EffectAddUtil.AddReason.SKILL, player);
				}
			if (range_effects != null)
				for (RangeEffect eff : range_effects[lv]) {
					MobEffectInstance ins = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(eff.id), eff.duration, eff.amplifier);
					for (Entity e : player.level.getEntities(player, new AABB(player.position(), player.position()).inflate(eff.range))) {
						if (e.distanceToSqr(player) > eff.range * eff.range) continue;
						if (!(e instanceof LivingEntity le)) continue;
						if (eff.for_enemy == TeamAccessor.arePlayerAndEntityInSameTeam(player, le)) continue;
						EffectAddUtil.addEffect(le, ins, EffectAddUtil.AddReason.SKILL, player);
					}
				}
		}

		@Override
		public boolean isValid() {
			if (!super.isValid()) return false;
			if (effects != null) {
				if (effects.length != max_level) {
					LightLand.LOGGER.error("effects length must be the same as max_level");
					return false;
				}
				for (int i = 0; i < max_level; i++)
					for (Effect e : effects[i]) {
						if (!ForgeRegistries.MOB_EFFECTS.containsKey(e.id)) {
							LightLand.LOGGER.error("effect " + e.id + " is invalid");
							return false;
						}
						if (e.duration <= 0 || e.duration > cooldown[i]) {
							LightLand.LOGGER.error("duration is invalid");
							return false;
						}
						if (e.amplifier < 0) {
							LightLand.LOGGER.error("amplifier is invalid");
							return false;
						}
					}
			}
			if (range_effects != null) {
				if (range_effects.length != max_level) {
					LightLand.LOGGER.error("range_effects length must be the same as max_level");
					return false;
				}
				for (int i = 0; i < max_level; i++)
					for (RangeEffect e : range_effects[i]) {
						if (!ForgeRegistries.MOB_EFFECTS.containsKey(e.id)) {
							LightLand.LOGGER.error("effect " + e.id + " is invalid");
							return false;
						}
						if (e.duration <= 0 || e.duration > cooldown[i]) {
							LightLand.LOGGER.error("duration is invalid");
							return false;
						}
						if (e.amplifier < 0) {
							LightLand.LOGGER.error("amplifier is invalid");
							return false;
						}
						if (e.range < 2 || e.range > 64) {
							LightLand.LOGGER.error("range is invalid");
							return false;
						}
					}
			}
			return true;
		}
	}

	@Override
	public void activate(Level level, ServerPlayer player, SkillData data) {
		Config config = getConfig();
		if (config != null) config.activate(player, data);
		super.activate(level, player, data);
	}

	public SkillData genData(Player player) {
		return new SkillData();
	}


}
