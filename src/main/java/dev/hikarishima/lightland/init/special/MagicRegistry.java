package dev.hikarishima.lightland.init.special;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.magic.products.MagicElement;
import dev.hikarishima.lightland.content.magic.products.MagicProductType;
import dev.hikarishima.lightland.init.LightLand;

public class MagicRegistry {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends MagicProductType<?, ?>> RegistryEntry<T> regProd(String id, NonNullSupplier<T> sup) {
        return LightLand.REGISTRATE.generic((Class<MagicProductType<?, ?>>) (Class) MagicProductType.class, id, sup).defaultLang().register();
    }

    public static <T extends MagicElement> RegistryEntry<T> regElem(String id, NonNullSupplier<T> sup) {
        return LightLand.REGISTRATE.generic(MagicElement.class, id, sup).defaultLang().register();
    }

    public static void register() {
    }

}
