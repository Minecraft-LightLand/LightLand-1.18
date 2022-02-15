package dev.hikarishima.lightland.content.skill;

import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.lcy0x1.base.NamedEntry;

public class Skill extends NamedEntry<Skill> {

    public Skill() {
        super(() -> LightLandRegistry.SKILL);
    }

}
