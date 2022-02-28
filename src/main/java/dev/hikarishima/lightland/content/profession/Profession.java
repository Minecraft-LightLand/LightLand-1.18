package dev.hikarishima.lightland.content.profession;

import dev.hikarishima.lightland.content.common.capability.AbilityPoints;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.lcy0x1.base.NamedEntry;
import net.minecraft.resources.ResourceLocation;

public abstract class Profession extends NamedEntry<Profession> {

    public Profession() {
        super(() -> LightLandRegistry.PROFESSION);
    }

    public abstract void init(LLPlayerData handler);

    /**
     * only AbilityPoints is allowed to change
     */
    public abstract void levelUp(LLPlayerData handler);

    public ResourceLocation getIcon() {
        ResourceLocation rl = getRegistryName();
        return new ResourceLocation(rl.getNamespace(), "textures/profession/" + rl.getPath() + ".png");
    }

    /**
     * return null if pass
     * return failure reason if failed
     */
    public LangData.IDS allowLevel(AbilityPoints.LevelType type, LLPlayerData handler) {
        int level = handler.abilityPoints.level;
        if (type == AbilityPoints.LevelType.SPELL) {
            int lv = handler.magicAbility.spell_level;
            if (lv == 9)
                return LangData.IDS.LVUP_MAX_REACHED;
            if (level < 10) {
                if (this == LightLandRegistry.PROF_SPELL.get())
                    return null;
                if (this == LightLandRegistry.PROF_MAGIC.get() || this == LightLandRegistry.PROF_ARCANE.get())
                    return lv >= 3 ? LangData.IDS.LVUP_PROF_MAX : null;
                if (lv >= 1)
                    return LangData.IDS.LVUP_PROF_MAX;
            }
        } else if (type == AbilityPoints.LevelType.MANA) {
            int lv = handler.magicAbility.magic_level;
            if (lv >= 10)
                return LangData.IDS.LVUP_MAX_REACHED;
            if (level < 10) {
                if (this == LightLandRegistry.PROF_MAGIC.get())
                    return null;
                if (this == LightLandRegistry.PROF_SPELL.get() || this == LightLandRegistry.PROF_ARCANE.get())
                    return lv >= 3 + level / 3 ? LangData.IDS.LVUP_PROF_MAX : null;
                if (lv >= 3)
                    return LangData.IDS.LVUP_PROF_MAX;
            }
        } else {
            int lv = type.level.apply(handler);
            if (lv >= 10)
                return LangData.IDS.LVUP_MAX_REACHED;
            if (level < 10) {
                if (this == LightLandRegistry.PROF_MAGIC.get() || this == LightLandRegistry.PROF_SPELL.get())
                    return lv >= 1 + level / 5 ? LangData.IDS.LVUP_PROF_MAX : null;
                if (this == LightLandRegistry.PROF_ARCANE.get())
                    return lv >= 3 + level / 3 ? LangData.IDS.LVUP_PROF_MAX : null;
            }
        }
        return null;
    }

}
