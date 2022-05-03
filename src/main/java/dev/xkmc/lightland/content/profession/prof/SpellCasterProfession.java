package dev.xkmc.lightland.content.profession.prof;

import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.profession.Profession;
import dev.xkmc.lightland.init.special.MagicRegistry;

public class SpellCasterProfession extends Profession {

	@Override
	public void init(LLPlayerData handler) {
		handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_EARTH.get());
		handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_WATER.get());
		handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_AIR.get());
		handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_FIRE.get());
		handler.magicAbility.spell_level += 2;
		handler.abilityPoints.magic++;
		handler.abilityPoints.general++;
	}

	@Override
	public void levelUp(LLPlayerData handler) {
		handler.abilityPoints.general++;
		handler.abilityPoints.magic++;
		handler.abilityPoints.element++;
	}

}
