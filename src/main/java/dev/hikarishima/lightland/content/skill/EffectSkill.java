package dev.hikarishima.lightland.content.skill;

import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.util.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public static class Config extends SkillConfig<SkillData> {

        @SerialClass.SerialField
        public Effect[][] effects;

        @SuppressWarnings("ConstantConditions")
        public List<MobEffectInstance> getIns(SkillData data) {
            int lv = Math.min(data.level, effects.length - 1);
            return Arrays.stream(effects[lv]).map(e ->
                            new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(e.id), e.duration, e.amplifier))
                    .collect(Collectors.toList());
        }

        @Override
        public boolean isValid() {
            if (!super.isValid()) return false;
            if (effects == null || effects.length != max_level) {
                LightLand.LOGGER.error("effects length must be the same as max_level");
                return false;
            }
            for (int i = 0; i < max_level; i++)
                for (Effect e : effects[i]) {
                    if (!ForgeRegistries.MOB_EFFECTS.containsKey(e.id)) {
                        LightLand.LOGGER.error("effect " + e.id + " is invalids");
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
            return true;
        }
    }

    @Override
    public void activate(Level level, ServerPlayer player, SkillData data) {
        getConfig().getIns(data).forEach(player::addEffect);
        super.activate(level, player, data);
    }

    public SkillData genData(Player player) {
        return new SkillData();
    }


}
