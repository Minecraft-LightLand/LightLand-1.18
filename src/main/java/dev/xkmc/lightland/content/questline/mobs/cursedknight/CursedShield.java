package dev.xkmc.lightland.content.questline.mobs.cursedknight;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class CursedShield extends BaseCursedKnight<CursedShield> {

	public CursedShield(EntityType<CursedShield> type, Level level) {
		super(type, level, CursedKnightProperties.CONFIG_SHIELD, MobEffects.DAMAGE_RESISTANCE);
	}
}
