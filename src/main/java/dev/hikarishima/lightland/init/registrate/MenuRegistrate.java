package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.hikarishima.lightland.content.common.item.backpack.BackpackContainer;
import dev.hikarishima.lightland.content.common.item.backpack.BackpackScreen;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

/**
 * handles container menu
 */
public class MenuRegistrate {

    public static final MenuEntry<BackpackContainer> MT_BACKPACK = REGISTRATE.menu("backpack",
            BackpackContainer::fromNetwork, () -> BackpackScreen::new).register();

    public static void register() {

    }

}
