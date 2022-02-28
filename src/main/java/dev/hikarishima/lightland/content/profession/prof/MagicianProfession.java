package dev.hikarishima.lightland.content.profession.prof;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.profession.Profession;

public class MagicianProfession extends Profession {

    @Override
    public void init(LLPlayerData handler) {
        //handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_EARTH);
        //handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_WATER);
        //handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_AIR);
        //handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_FIRE);
        handler.magicAbility.magic_level += 2;
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
