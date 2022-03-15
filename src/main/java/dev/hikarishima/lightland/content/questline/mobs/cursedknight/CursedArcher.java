package dev.hikarishima.lightland.content.questline.mobs.cursedknight;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class CursedArcher extends BaseCursedKnight<CursedArcher> {

	public CursedArcher(EntityType<CursedArcher> type, Level level) {
		super(type, level, CursedKnightProperties.CONFIG_ARCHER, MobEffects.MOVEMENT_SPEED);
	}

}
