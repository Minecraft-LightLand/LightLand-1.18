package dev.hikarishima.lightland.content.profession;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;

public abstract class CombatProfession extends Profession {

    @Override
    public final void init(LLPlayerData handler) {
        handler.abilityPoints.general++;
        handler.abilityPoints.body += 3;
    }

    @Override
    public final void levelUp(LLPlayerData handler) {
        handler.abilityPoints.general++;
        if (handler.abilityPoints.level <= 10)
            handler.abilityPoints.body++;
        if (handler.abilityPoints.level % 2 == 0)
            handler.abilityPoints.element++;
    }

}
