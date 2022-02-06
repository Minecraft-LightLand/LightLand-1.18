package dev.hikarishima.lightland.content.profession;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;

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
