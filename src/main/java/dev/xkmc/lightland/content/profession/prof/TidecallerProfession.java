package dev.xkmc.lightland.content.profession.prof;

import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.profession.Profession;

public class TidecallerProfession extends Profession {

	@Override
	public void init(LLPlayerData handler) {
		handler.abilityPoints.general += 4;
		//handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_WATER);
		//handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_WATER);
		handler.abilityPoints.element++;
	}

	@Override
	public void levelUp(LLPlayerData handler) {
		handler.abilityPoints.general++;
		if (handler.abilityPoints.level <= 10)
			handler.abilityPoints.general++;
		if (handler.abilityPoints.level % 2 == 0)
			handler.abilityPoints.element++;
	}

}
