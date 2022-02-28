package dev.hikarishima.lightland.content.skill;

import dev.hikarishima.lightland.compat.TeamAccessor;
import dev.hikarishima.lightland.content.skill.internal.Skill;
import dev.hikarishima.lightland.content.skill.internal.SkillConfig;
import dev.hikarishima.lightland.content.skill.internal.SkillData;
import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class ImpactSkill extends Skill<ImpactSkill.Config, SkillData> {

    @SerialClass
    public static class Config extends SkillConfig<SkillData> {

        @SerialClass.SerialField
        public double[] radius;

        @SerialClass.SerialField
        public double[] impact;

        @SerialClass.SerialField
        public double[] damage;

        @Override
        public boolean isValid() {
            if (!super.isValid()) return false;
            if (radius.length != max_level) {
                LightLand.LOGGER.error("radius length must be the same as max level");
                return false;
            }
            if (impact.length != max_level) {
                LightLand.LOGGER.error("impact length must be the same as max level");
                return false;
            }
            if (damage.length != max_level) {
                LightLand.LOGGER.error("damage length must be the same as max level");
                return false;
            }
            for (double val : radius) {
                if (val < 2 || val > 32) {
                    LightLand.LOGGER.error("radius must be between 2 and 32");
                    return false;
                }
            }
            for (double val : impact) {
                if (val < -3 || val > 3) {
                    LightLand.LOGGER.error("radius must be between -3 and 3");
                    return false;
                }
            }
            for (double val : damage) {
                if (val < 0 || val > 100) {
                    LightLand.LOGGER.error("radius must be between 0 and 100");
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void activate(Level level, ServerPlayer player, SkillData data) {
        Config config = getConfig();
        if (config != null) {
            int lv = Math.min(config.max_level, data.level);
            double radius = config.radius[lv];
            double impact = config.radius[lv];
            double damage = config.radius[lv];
            DamageSource source = DamageSource.playerAttack(player);
            for (Entity e : level.getEntities(player, new AABB(player.position(), player.position()).inflate(radius))) {
                if (e.distanceToSqr(player) > radius * radius) continue;
                if (e instanceof LivingEntity le && TeamAccessor.arePlayerAndEntityInSameTeam(player, le)) continue;
                e.hasImpulse = true;
                e.setDeltaMovement(e.getPosition(1).subtract(player.position()).normalize().scale(impact));
                if (!(e instanceof LivingEntity le)) continue;
                le.hurt(source, (float) damage);
            }
        }
        super.activate(level, player, data);
    }

    @Override
    public SkillData genData(Player player) {
        return new SkillData();
    }

}
