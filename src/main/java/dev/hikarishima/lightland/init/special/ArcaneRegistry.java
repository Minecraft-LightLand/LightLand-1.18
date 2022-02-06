package dev.hikarishima.lightland.init.special;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.magic.DamageAxe;
import dev.hikarishima.lightland.content.arcane.magic.MarkerSword;
import dev.hikarishima.lightland.content.arcane.magic.ThunderAxe;
import dev.hikarishima.lightland.content.arcane.magic.ThunderSword;
import dev.hikarishima.lightland.init.LightLand;

@SuppressWarnings("unused")
public class ArcaneRegistry {

    public static final int ARCANE_TIME = 600;

    public static final RegistryEntry<ThunderAxe> MERAK_THUNDER = reg("thunder_axe", () -> new ThunderAxe(10, 64f));
    public static final RegistryEntry<ThunderSword> ALKAID_THUNDER = reg("thunder_sword", () -> new ThunderSword(20, 64f));
    public static final RegistryEntry<MarkerSword> ALIOTH_MARKER = reg("marker", () -> new MarkerSword(30, 32));
    public static final RegistryEntry<DamageAxe> DUBHE_DAMAGE = reg("damage_axe", () -> new DamageAxe(100, 30));
    //public static final WindBladeSword ALIOTH_WINDBLADE = reg("wind_blade", new WindBladeSword(5f, 1f, 64f));

    private static <T extends Arcane> RegistryEntry<T> reg(String str, NonNullSupplier<T> a) {
        return LightLand.REGISTRATE.generic(Arcane.class, str, a).defaultLang().register();
    }

    public static void register(){}

}
