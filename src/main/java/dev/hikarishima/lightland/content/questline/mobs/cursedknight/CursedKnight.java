package dev.hikarishima.lightland.content.questline.mobs.cursedknight;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class CursedKnight extends BaseCursedKnight<CursedKnight> {

	public CursedKnight(EntityType<CursedKnight> type, Level level) {
		super(type, level, CursedKnightProperties.CONFIG_KNIGHT, MobEffects.DAMAGE_BOOST);
	}
}
