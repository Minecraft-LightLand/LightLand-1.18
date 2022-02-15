package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.content.arcane.item.ArcaneAxe;
import dev.hikarishima.lightland.content.arcane.item.ArcaneSword;
import dev.hikarishima.lightland.content.archery.feature.FeatureList;
import dev.hikarishima.lightland.content.archery.feature.arrow.*;
import dev.hikarishima.lightland.content.archery.feature.bow.DefaultShootFeature;
import dev.hikarishima.lightland.content.archery.feature.bow.EnderShootFeature;
import dev.hikarishima.lightland.content.archery.feature.bow.GlowTargetAimFeature;
import dev.hikarishima.lightland.content.archery.feature.bow.WindBowFeature;
import dev.hikarishima.lightland.content.archery.item.GenericArrowItem;
import dev.hikarishima.lightland.content.archery.item.GenericBowItem;
import dev.hikarishima.lightland.content.magic.item.MagicScroll;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.util.NonNullLazy;

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
    public static final ItemEntry<GenericBowItem> EAGLE_BOW;
    public static final ItemEntry<GenericBowItem> WIND_BOW;

    public static final ItemEntry<GenericArrowItem> STARTER_ARROW;
    public static final ItemEntry<GenericArrowItem> COPPER_ARROW, IRON_ARROW, OBSIDIAN_ARROW;
    public static final ItemEntry<GenericArrowItem> NO_FALL_ARROW;
    public static final ItemEntry<GenericArrowItem> ENDER_ARROW;
    public static final ItemEntry<GenericArrowItem> TNT_1_ARROW, TNT_2_ARROW, TNT_3_ARROW;
    public static final ItemEntry<GenericArrowItem> FIRE_1_ARROW, FIRE_2_ARROW;
    public static final ItemEntry<GenericArrowItem> ICE_ARROW;
    public static final ItemEntry<GenericArrowItem> DISPELL_ARROW;


    public static final ItemEntry<ArcaneSword> ARCANE_SWORD_GILDED = REGISTRATE.item("gilded_arcane_sword", p ->
                    new ArcaneSword(Tiers.IRON, 5, -2.4f, p.stacksTo(1).setNoRepair(), 50))
            .model((ctx, pvd) -> pvd.handheld(ctx::getEntry)).defaultLang().register();
    public static final ItemEntry<ArcaneAxe> ARCANE_AXE_GILDED = REGISTRATE.item("gilded_arcane_axe", p ->
                    new ArcaneAxe(Tiers.IRON, 8, -3.1f, p.stacksTo(1).setNoRepair(), 50))
            .model((ctx, pvd) -> pvd.handheld(ctx::getEntry)).defaultLang().register();

    public static final ItemEntry<MagicScroll> SPELL_CARD = REGISTRATE.item("spell_card", p ->
                    new MagicScroll(MagicScroll.ScrollType.CARD, p))
            .defaultModel().defaultLang().register();
    public static final ItemEntry<MagicScroll> SPELL_PARCHMENT = REGISTRATE.item("spell_parchment", p ->
                    new MagicScroll(MagicScroll.ScrollType.PARCHMENT, p))
            .defaultModel().defaultLang().register();
    public static final ItemEntry<MagicScroll> SPELL_SCROLL = REGISTRATE.item("spell_scroll", p ->
                    new MagicScroll(MagicScroll.ScrollType.SCROLL, p))
            .defaultModel().defaultLang().register();

    static {
        STARTER_BOW = genBow("starter_bow", 600, 0, 0, FeatureList::end);
        IRON_BOW = genBow("iron_bow", 1200, 1, 0, 40, 3.9f, FeatureList::end);
        MAGNIFY_BOW = genBow("magnify_bow", 600, 0, 0, 20, 3.0f, 60, 0.9f, e -> e.add(new GlowTargetAimFeature(128)));
        GLOW_AIM_BOW = genBow("glow_aim_bow", 600, 0, 0, e -> e.add(new GlowTargetAimFeature(128)));
        ENDER_AIM_BOW = genBow("ender_aim_bow", 8, -1, 0, e -> e.add(new EnderShootFeature(128)));
        EAGLE_BOW = genBow("eagle_bow", 600, 6, 2, 40, 3f, e -> e.add(new DamageArrowFeature(
                a -> DamageSource.arrow(a, a.getOwner()).bypassArmor(),
                a -> (float) (a.getBaseDamage() * a.getDeltaMovement().length())
        )));
        WIND_BOW = genBow("wind_bow", 600, 0, 1, 10, 3.9f, e -> e
                .add(new NoFallArrowFeature(40))
                .add(new WindBowFeature()));

        STARTER_ARROW = genArrow("starter_arrow", 0, 0, true, FeatureList::end);
        COPPER_ARROW = genArrow("copper_arrow", 1, 0, false, FeatureList::end);
        IRON_ARROW = genArrow("iron_arrow", 1, 1, false, FeatureList::end);
        OBSIDIAN_ARROW = genArrow("obsidian_arrow", 1.5f, 0, false, FeatureList::end);
        NO_FALL_ARROW = genArrow("no_fall_arrow", 0, 0, false, e -> e.add(new NoFallArrowFeature(40)));
        ENDER_ARROW = genArrow("ender_arrow", -1, 0, false, e -> e.add(new EnderArrowFeature()));
        TNT_1_ARROW = genArrow("tnt_arrow_lv1", 0, 0, false, e -> e.add(new ExplodeArrowFeature(2)));
        TNT_2_ARROW = genArrow("tnt_arrow_lv2", 0, 0, false, e -> e.add(new ExplodeArrowFeature(4)));
        TNT_3_ARROW = genArrow("tnt_arrow_lv3", 0, 0, false, e -> e.add(new ExplodeArrowFeature(6)));
        FIRE_1_ARROW = genArrow("fire_arrow_lv1", 0, 0, false, e -> e
                .add(new FireArrowFeature(100)).add(new PotionArrowFeature(
                        new MobEffectInstance(VanillaMagicRegistrate.FLAME.get(), 100, 0))));
        FIRE_2_ARROW = genArrow("fire_arrow_lv2", 0, 0, false, e -> e
                .add(new FireArrowFeature(200)).add(new PotionArrowFeature(
                        new MobEffectInstance(VanillaMagicRegistrate.FLAME.get(), 100, 1))));
        ICE_ARROW = genArrow("frozen_arrow", 0, 0, false, e -> e.add(new PotionArrowFeature(
                new MobEffectInstance(VanillaMagicRegistrate.ICE.get(), 600),
                new MobEffectInstance(VanillaMagicRegistrate.WATER_TRAP.get(), 200))));
        DISPELL_ARROW = genArrow("dispell_arrow", 0, 0, false, e -> e.add(new DamageArrowFeature(
                a -> DamageSource.arrow(a, a.getOwner()).bypassMagic(),
                a -> (float) (a.getBaseDamage() * a.getDeltaMovement().length())
        )));
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
        NonNullLazy<FeatureList> f = NonNullLazy.of(() -> {
            FeatureList features = new FeatureList();
            consumer.accept(features);
            return features;
        });
        return REGISTRATE.item(id, p -> new GenericArrowItem(p, new GenericArrowItem.ArrowConfig(damage, punch, is_inf, f)))
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
