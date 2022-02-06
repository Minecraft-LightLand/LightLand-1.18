package dev.hikarishima.lightland.content.arcane;

import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.magic.DamageAxe;
import dev.hikarishima.lightland.content.arcane.magic.MarkerSword;
import dev.hikarishima.lightland.content.arcane.magic.ThunderAxe;
import dev.hikarishima.lightland.content.arcane.magic.ThunderSword;
import dev.hikarishima.lightland.init.LightLand;

@SuppressWarnings("unused")
public class ArcaneRegistry {

    public static final int ARCANE_TIME = 600;

    public static final ThunderAxe MERAK_THUNDER = reg("thunder_axe", new ThunderAxe(10, 64f));
    public static final ThunderSword ALKAID_THUNDER = reg("thunder_sword", new ThunderSword(20, 64f));
    //public static final WindBladeSword ALIOTH_WINDBLADE = reg("wind_blade", new WindBladeSword(5f, 1f, 64f));
    public static final MarkerSword ALIOTH_MARKER = reg("marker", new MarkerSword(30, 32));
    public static final DamageAxe DUBHE_DAMAGE = reg("damage_axe", new DamageAxe(100, 30));

    private static <T extends Arcane> T reg(String str, T a) {
        a.setRegistryName(LightLand.MODID, str);
        return a;
    }

}
