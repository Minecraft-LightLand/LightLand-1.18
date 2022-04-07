package dev.hikarishima.lightland.content.skill.internal;

import dev.lcy0x1.serial.SerialClass;

@SerialClass
public class SkillData {

	@SerialClass.SerialField
	public int level;

	@SerialClass.SerialField
	public int cooldown;

}
