package dev.hikarishima.lightland.content.profession.prof;

import dev.hikarishima.lightland.content.common.capability.player.LLPlayerData;
import dev.hikarishima.lightland.content.profession.Profession;

public abstract class SemiMagicProfession extends Profession {

	@Override
	public final void init(LLPlayerData handler) {
		handler.abilityPoints.general += 2;
		handler.abilityPoints.magic += 2;
		handler.abilityPoints.element += 3;
	}

	@Override
	public final void levelUp(LLPlayerData handler) {
		handler.abilityPoints.general++;
		if (handler.abilityPoints.level <= 10)
			handler.abilityPoints.magic++;
		handler.abilityPoints.element++;
	}

}
