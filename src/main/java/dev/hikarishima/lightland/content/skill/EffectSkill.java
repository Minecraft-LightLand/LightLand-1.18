package dev.hikarishima.lightland.content.skill;

import dev.lcy0x1.util.SerialClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectSkill extends Skill<EffectSkill.Config, SkillData> {

    @SerialClass
    public static class Config extends SkillConfig<SkillData> {

        @SerialClass.SerialField
        public ResourceLocation id;

        @SerialClass.SerialField
        public int[] durations;

        @SerialClass.SerialField
        public int[] amplifiers;

        @SuppressWarnings("ConstantConditions")
        public MobEffectInstance getIns(SkillData data) {
            int lv = Math.min(data.level, durations.length - 1);
            return new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(id), durations[lv], amplifiers[lv]);
        }

        @Override
        public boolean isValid() {
            if (super.isValid() && id != null && ForgeRegistries.MOB_EFFECTS.containsKey(id) &&
                    durations.length == max_level && amplifiers.length == max_level) {
                for (int val : durations) {
                    if (val <= 0) {
                        return false;
                    }
                }
                for (int val : amplifiers) {
                    if (val <= 0) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public void activate(Level level, ServerPlayer player, SkillData data) {
        player.addEffect(getConfig().getIns(data));
        super.activate(level, player, data);
    }

    public SkillData genData(Player player) {
        return new SkillData();
    }


}
