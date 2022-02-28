package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.hikarishima.lightland.content.common.item.backpack.BackpackContainer;
import dev.hikarishima.lightland.content.common.item.backpack.BackpackScreen;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.inventory.MenuType;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

/**
 * handles container menu
 */
@MethodsReturnNonnullByDefault
public class MenuRegistrate {

    public static final MenuEntry<BackpackContainer> MT_BACKPACK = REGISTRATE.menu("backpack",
            BackpackContainer::fromNetwork, () -> BackpackScreen::new).lang(MenuRegistrate::getLangKey).register();

    public static void register() {

    }

    public static String getLangKey(MenuType<?> menu) {
        return "container." + menu.getRegistryName().getNamespace() + "." + menu.getRegistryName().getPath();
    }

}
