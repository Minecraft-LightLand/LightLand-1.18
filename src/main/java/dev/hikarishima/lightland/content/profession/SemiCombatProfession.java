package dev.hikarishima.lightland.content.profession;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;

public abstract class SemiCombatProfession extends Profession {

    @Override
    public final void init(LLPlayerData handler) {
        handler.abilityPoints.general += 2;
        handler.abilityPoints.body += 2;
        handler.abilityPoints.element++;
    }

    @Override
    public final void levelUp(LLPlayerData handler) {
        handler.abilityPoints.general++;
        if (handler.abilityPoints.level <= 100)
            handler.abilityPoints.body++;
        if (handler.abilityPoints.level % 2 == 0)
            handler.abilityPoints.element++;
    }

}
