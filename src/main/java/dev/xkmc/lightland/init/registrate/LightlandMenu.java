package dev.xkmc.lightland.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.MenuEntry;
import dev.xkmc.lightland.content.magic.gui.craft.*;
import dev.xkmc.lightland.init.LightLand;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.inventory.MenuType;

/**
 * handles container menu
 */
@MethodsReturnNonnullByDefault
public class LightlandMenu {

	public static final MenuEntry<ArcaneInjectContainer> MT_ARCANE = LightLand.REGISTRATE.menu("arcane_inject",
			ArcaneInjectContainer::new, () -> ArcaneInjectScreen::new).lang(LightlandMenu::getLangKey).register();
	public static final MenuEntry<DisEnchanterContainer> MT_DISENC = LightLand.REGISTRATE.menu("disenchanter",
			DisEnchanterContainer::new, () -> DisEnchanterScreen::new).lang(LightlandMenu::getLangKey).register();
	public static final MenuEntry<SpellCraftContainer> MT_SPCRAFT = LightLand.REGISTRATE.menu("spell_craft",
			SpellCraftContainer::new, () -> SpellCraftScreen::new).lang(LightlandMenu::getLangKey).register();

	public static void register() {

	}

	public static String getLangKey(MenuType<?> menu) {
		return "container." + menu.getRegistryName().getNamespace() + "." + menu.getRegistryName().getPath();
	}

}
