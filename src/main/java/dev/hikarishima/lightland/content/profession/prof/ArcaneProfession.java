package dev.hikarishima.lightland.content.profession.prof;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.profession.Profession;

public class ArcaneProfession extends Profession {

    @Override
    public void init(LLPlayerData handler) {
        handler.abilityPoints.arcane += 2;
        handler.abilityPoints.general += 2;
        //handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_QUINT);
        handler.abilityPoints.element++;
    }

    @Override
    public void levelUp(LLPlayerData handler) {
        handler.abilityPoints.general++;
        if (handler.abilityPoints.level <= 10)
            handler.abilityPoints.general++;
        handler.abilityPoints.element++;
    }

}
