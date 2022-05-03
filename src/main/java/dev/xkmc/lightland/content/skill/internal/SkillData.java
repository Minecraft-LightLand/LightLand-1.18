package dev.xkmc.lightland.content.skill.internal;

import dev.xkmc.l2library.serial.SerialClass;

@SerialClass
public class SkillData {

	@SerialClass.SerialField
	public int level;

	@SerialClass.SerialField
	public int cooldown;

}
