package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.content.archery.feature.FeatureList;
import dev.hikarishima.lightland.content.archery.feature.arrow.EnderArrowFeature;
import dev.hikarishima.lightland.content.archery.feature.arrow.NoFallArrowFeature;
import dev.hikarishima.lightland.content.archery.feature.bow.DefaultShootFeature;
import dev.hikarishima.lightland.content.archery.feature.bow.EnderShootFeature;
import dev.hikarishima.lightland.content.archery.feature.bow.GlowTargetAimFeature;
import dev.hikarishima.lightland.content.archery.item.GenericArrowItem;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Consumer;

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

    public static final ItemEntry<GenericBowItem> STARTER_BOW;
    public static final ItemEntry<GenericBowItem> IRON_BOW;
    public static final ItemEntry<GenericBowItem> MAGNIFY_BOW;
    public static final ItemEntry<GenericBowItem> GLOW_AIM_BOW;
    public static final ItemEntry<GenericBowItem> ENDER_AIM_BOW;

    public static final ItemEntry<GenericArrowItem> STARTER_ARROW;
    public static final ItemEntry<GenericArrowItem> COPPER_ARROW;
    public static final ItemEntry<GenericArrowItem> IRON_ARROW;
    public static final ItemEntry<GenericArrowItem> OBSIDIAN_ARROW;
    public static final ItemEntry<GenericArrowItem> NO_FALL_ARROW;
    public static final ItemEntry<GenericArrowItem> ENDER_ARROW;

    static {
        STARTER_BOW = genBow("starter_bow", 600, 0, 0, FeatureList::end);
        IRON_BOW = genBow("iron_bow", 1200, 1, 0, 40, 3.9f, FeatureList::end);
        MAGNIFY_BOW = genBow("magnify_bow", 600, 0, 0, 20, 3.0f, 60, 0.9f, e -> e.add(new GlowTargetAimFeature(128)));
        GLOW_AIM_BOW = genBow("glow_aim_bow", 600, 0, 0, e -> e.add(new GlowTargetAimFeature(128)));
        ENDER_AIM_BOW = genBow("ender_aim_bow", 8, -1, 0, e -> e.add(new EnderShootFeature(128)));

        STARTER_ARROW = genArrow("starter_arrow", 0, 0, true, FeatureList::end);
        COPPER_ARROW = genArrow("copper_arrow", 1, 0, true, FeatureList::end);
        IRON_ARROW = genArrow("iron_arrow", 1, 1, true, FeatureList::end);
        OBSIDIAN_ARROW = genArrow("obsidian_arrow", 1.5f, 0, true, FeatureList::end);
        NO_FALL_ARROW = genArrow("no_fall_arrow", 0, 0, false, e -> e.add(new NoFallArrowFeature(40)));
        ENDER_ARROW = genArrow("ender_arrow", -1, 0, false, e -> e.add(new EnderArrowFeature(128)));
    }

    public static void register() {
    }

    public static ItemEntry<GenericBowItem> genBow(String id, int durability, float damage, int punch, Consumer<FeatureList> consumer) {
        return genBow(id, durability, damage, punch, 20, 3.0f, 20, 0.15f, consumer);
    }

    public static ItemEntry<GenericBowItem> genBow(String id, int durability, float damage, int punch, int pull_time, float speed, Consumer<FeatureList> consumer) {
        return genBow(id, durability, damage, punch, pull_time, speed, pull_time, 0.15f, consumer);
    }

    public static ItemEntry<GenericBowItem> genBow(String id, int durability, float damage, int punch, int pull_time, float speed, int fov_time, float fov, Consumer<FeatureList> consumer) {
        FeatureList features = new FeatureList().add(DefaultShootFeature.INSTANCE);
        consumer.accept(features);
        return REGISTRATE.item(id, p -> new GenericBowItem(p.stacksTo(1).durability(durability),
                        new GenericBowItem.BowConfig(damage, punch, pull_time, speed, fov_time, fov, features)))
                .model(ItemRegistrate::createBowModel).defaultLang().register();
    }

    public static ItemEntry<GenericArrowItem> genArrow(String id, float damage, int punch, boolean is_inf, Consumer<FeatureList> consumer) {
        FeatureList features = new FeatureList();
        consumer.accept(features);
        return REGISTRATE.item(id, p -> new GenericArrowItem(p, new GenericArrowItem.ArrowConfig(damage, punch, is_inf, features)))
                .defaultModel().defaultLang().register();
    }

    private static final float[] BOW_PULL_VALS = {0, 0.65f, 0.9f};

    public static <T extends GenericBowItem> void createBowModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
        ItemModelBuilder builder = pvd.withExistingParent(ctx.getName(), "minecraft:bow");
        builder.texture("layer0", "item/" + ctx.getName());
        for (int i = 0; i < 3; i++) {
            String name = ctx.getName() + "_pulling_" + i;
            ItemModelBuilder ret = pvd.getBuilder(name).parent(new ModelFile.UncheckedModelFile("minecraft:item/bow_pulling_" + i));
            ret.texture("layer0", "item/" + name);
            ItemModelBuilder.OverrideBuilder override = builder.override();
            override.predicate(new ResourceLocation("pulling"), 1);
            if (BOW_PULL_VALS[i] > 0)
                override.predicate(new ResourceLocation("pull"), BOW_PULL_VALS[i]);
            override.model(new ModelFile.UncheckedModelFile(LightLand.MODID + ":item/" + name));
        }
    }

}
