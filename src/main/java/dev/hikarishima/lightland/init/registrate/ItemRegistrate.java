package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.builders.ItemBuilder;
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
import dev.hikarishima.lightland.content.berserker.item.MedicineArmor;
import dev.hikarishima.lightland.content.berserker.item.MedicineLeather;
import dev.hikarishima.lightland.content.common.item.Mat;
import dev.hikarishima.lightland.content.common.item.backpack.BackpackItem;
import dev.hikarishima.lightland.content.magic.item.MagicScroll;
import dev.hikarishima.lightland.content.magic.item.MagicWand;
import dev.hikarishima.lightland.content.magic.item.ManaStorage;
import dev.hikarishima.lightland.content.magic.item.PotionCore;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.special.LLRegistrate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.util.NonNullLazy;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

@SuppressWarnings({"rawtypes", "unchecked", "unsafe"})
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

    // -------- common --------
    public static final ItemEntry<BackpackItem>[] BACKPACKS;

    static {
        BACKPACKS = new ItemEntry[16];
        for (int i = 0; i < 16; i++) {
            DyeColor color = DyeColor.values()[i];
            BACKPACKS[i] = REGISTRATE.item("backpack_" + color.getName(), p -> new BackpackItem(color, p.stacksTo(1)))
                    .model((ctx, pvd) -> pvd.withExistingParent(ctx.getName(), "lightland:backpack")).defaultLang().register();
        }
    }

    // -------- archery --------
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

    // -------- magic --------
    public static final int MANA = 256;

    public static final ItemEntry<ArcaneSword> ARCANE_SWORD_GILDED;
    public static final ItemEntry<ArcaneAxe> ARCANE_AXE_GILDED;
    public static final ItemEntry<MagicWand> MAGIC_WAND;
    public static final ItemEntry<PotionCore> POTION_CORE;
    public static final ItemEntry<MagicScroll> SPELL_CARD, SPELL_PARCHMENT, SPELL_SCROLL;
    public static final ItemEntry<ManaStorage> ENC_GOLD_NUGGET, ENC_GOLD_INGOT, COOKIE, MELON, CARROT, APPLE;

    static {
        ARCANE_SWORD_GILDED = REGISTRATE.item("gilded_arcane_sword", p ->
                        new ArcaneSword(Tiers.IRON, 5, -2.4f, p.stacksTo(1).setNoRepair(), 50))
                .model((ctx, pvd) -> pvd.handheld(ctx::getEntry)).defaultLang().register();
        ARCANE_AXE_GILDED = REGISTRATE.item("gilded_arcane_axe", p ->
                        new ArcaneAxe(Tiers.IRON, 8, -3.1f, p.stacksTo(1).setNoRepair(), 50))
                .model((ctx, pvd) -> pvd.handheld(ctx::getEntry)).defaultLang().register();
        MAGIC_WAND = REGISTRATE.item("magic_wand", MagicWand::new)
                .defaultModel().defaultLang().register();
        POTION_CORE = REGISTRATE.item("potion_core", PotionCore::new)
                .defaultModel().defaultLang().register();
        SPELL_CARD = REGISTRATE.item("spell_card", p ->
                        new MagicScroll(MagicScroll.ScrollType.CARD, p))
                .defaultModel().defaultLang().register();
        SPELL_PARCHMENT = REGISTRATE.item("spell_parchment", p ->
                        new MagicScroll(MagicScroll.ScrollType.PARCHMENT, p))
                .defaultModel().defaultLang().register();
        SPELL_SCROLL = REGISTRATE.item("spell_scroll", p ->
                        new MagicScroll(MagicScroll.ScrollType.SCROLL, p))
                .defaultModel().defaultLang().register();
        COOKIE = REGISTRATE.item("enchant_cookie", p -> new ManaStorage(p.food(Foods.COOKIE), Items.COOKIE, MANA / 8))
                .defaultModel().defaultLang().register();
        MELON = REGISTRATE.item("enchant_melon", p -> new ManaStorage(p.food(Foods.MELON_SLICE), Items.MELON_SLICE, MANA))
                .defaultModel().defaultLang().register();
        CARROT = REGISTRATE.item("enchant_carrot", p -> new ManaStorage(p.food(Foods.GOLDEN_CARROT), Items.GOLDEN_CARROT, MANA * 8))
                .defaultModel().defaultLang().register();
        APPLE = REGISTRATE.item("enchant_apple", p -> new ManaStorage(p.food(Foods.GOLDEN_APPLE), Items.GOLDEN_APPLE, MANA * 72))
                .defaultModel().defaultLang().register();
        ENC_GOLD_NUGGET = REGISTRATE.item("enchant_gold_nugget", p -> new ManaStorage(p, Items.GOLD_NUGGET, MANA))
                .defaultModel().defaultLang().register();
        ENC_GOLD_INGOT = REGISTRATE.item("enchant_gold_ingot", p -> new ManaStorage(p, Items.GOLD_INGOT, MANA * 9))
                .defaultModel().defaultLang().register();
    }

    // -------- berserker --------
    public static final ItemEntry<MedicineLeather> MEDICINE_LEATHER = REGISTRATE.item("medicine_leather", p -> new MedicineLeather(14, p))
            .defaultModel().defaultLang().register();
    public static final ItemEntry<Item> KING_LEATHER = REGISTRATE.item("king_leather", Item::new)
            .defaultModel().defaultLang().register();
    public static final ItemEntry<MedicineLeather> KING_MED_LEATHER = REGISTRATE.item("king_med_leather", p -> new MedicineLeather(100, p))
            .defaultModel().defaultLang().register();
    public static final ItemEntry<MedicineArmor>[] MEDICINE_ARMOR = genArmor("medicine_leather",
            (slot, p) -> new MedicineArmor(Mat.MEDICINE_LEATHER, slot, p), e -> e.model(ItemRegistrate::createDoubleLayerModel));
    public static final ItemEntry<MedicineArmor>[] KING_MED_ARMOR = genArmor("king_leather",
            (slot, p) -> new MedicineArmor(Mat.KING_LEATHER, slot, p), e -> e.model(ItemRegistrate::createDoubleLayerModel));


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

    @SuppressWarnings({"rawtypes", "unsafe", "unchecked"})
    public static <T extends ArmorItem> ItemEntry<T>[] genArmor(String id, BiFunction<EquipmentSlot, Item.Properties, T> sup, Function<ItemBuilder<T, LLRegistrate>, ItemBuilder<T, LLRegistrate>> func) {
        ItemEntry[] ans = new ItemEntry[4];
        ans[0] = func.apply(REGISTRATE.item(id + "_helmet", p -> sup.apply(EquipmentSlot.HEAD, p))).defaultLang().register();
        ans[1] = func.apply(REGISTRATE.item(id + "_chestplate", p -> sup.apply(EquipmentSlot.CHEST, p))).defaultLang().register();
        ans[2] = func.apply(REGISTRATE.item(id + "_leggings", p -> sup.apply(EquipmentSlot.LEGS, p))).defaultLang().register();
        ans[3] = func.apply(REGISTRATE.item(id + "_boots", p -> sup.apply(EquipmentSlot.FEET, p))).defaultLang().register();
        return ans;
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

    public static <T extends Item> void createDoubleLayerModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
        ItemModelBuilder builder = pvd.withExistingParent(ctx.getName(), "minecraft:generated");
        builder.texture("layer0", "item/" + ctx.getName());
        builder.texture("layer1", "item/" + ctx.getName() + "_overlay");
    }

}
