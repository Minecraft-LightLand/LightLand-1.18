package dev.hikarishima.lightland.content.profession;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;

public class SpellCasterProfession extends Profession {

    @Override
    public void init(LLPlayerData handler) {
        //handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_EARTH);
        //handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_WATER);
        //handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_AIR);
        //handler.magicHolder.addElementalMastery(MagicRegistry.ELEM_FIRE);
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
