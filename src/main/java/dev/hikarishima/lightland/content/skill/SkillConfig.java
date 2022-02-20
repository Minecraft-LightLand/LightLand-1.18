package dev.hikarishima.lightland.content.skill;

import dev.lcy0x1.util.SerialClass;

@SerialClass
public class SkillConfig<T extends SkillData> {

    @SerialClass.SerialField
    public int[] cooldown;

    @SerialClass.SerialField
    public int max_level;

    public int getCooldown(T data) {
        int lv = Math.min(data.level, cooldown.length - 1);
        return cooldown[lv];
    }

    public boolean isValid() {
        if (max_level <= 0 || cooldown.length != max_level)
            return false;
        for (int val : cooldown) {
            if (val <= 0)
                return false;
        }
        return true;
    }

}
