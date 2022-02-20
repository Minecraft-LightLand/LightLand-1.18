package dev.hikarishima.lightland.content.skill;

import dev.lcy0x1.util.SerialClass;

@SerialClass
public class SkillConfig<T extends SkillData> {

    @SerialClass.SerialField
    public int cooldown;

    @SerialClass.SerialField
    public int max_level;

    public int getCooldown(T data) {
        return cooldown;
    }

    public boolean isValid() {
        return cooldown > 0 && max_level > 0;
    }

}
