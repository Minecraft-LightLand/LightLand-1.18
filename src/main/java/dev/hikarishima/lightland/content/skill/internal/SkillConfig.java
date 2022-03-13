package dev.hikarishima.lightland.content.skill.internal;

import dev.hikarishima.lightland.init.LightLand;
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
		if (max_level <= 0) {
			LightLand.LOGGER.error("max_level must be positive");
			return false;
		}
		if (cooldown.length != max_level) {
			LightLand.LOGGER.error("cooldown length must be the same as max_level");
			return false;
		}
		for (int val : cooldown) {
			if (val <= 0) {
				LightLand.LOGGER.error("cooldown must be positive");
				return false;
			}
		}
		return true;
	}

}
