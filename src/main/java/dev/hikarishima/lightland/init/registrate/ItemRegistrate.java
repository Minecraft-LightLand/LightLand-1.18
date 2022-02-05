package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.content.archery.feature.FeatureList;
import dev.hikarishima.lightland.content.archery.item.GenericArrowItem;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

@MethodsReturnNonnullByDefault
public class ItemRegistrate {

    public static class Tab extends CreativeModeTab {

        public Tab() {
            super(LightLand.MODID);
        }

        @Override
        public ItemStack makeIcon() {
            return STARTER_BOW.asStack();
        }
    }

    public static final Tab TAB = new Tab();

    static {
        REGISTRATE.creativeModeTab(() -> TAB);
    }

    public static final ItemEntry<GenericBowItem> STARTER_BOW = REGISTRATE
            .item("starter_bow", p -> new GenericBowItem(p.stacksTo(1).durability(256).setNoRepair(),
                    new GenericBowItem.BowConfig(new FeatureList())))
            .register();

    public static final ItemEntry<GenericArrowItem> STARTER_ARROW = REGISTRATE
            .item("starter_arrow", p -> new GenericArrowItem(p,
                    new GenericArrowItem.ArrowConfig(true, new FeatureList())))
            .register();

    public static void register() {
    }

}
