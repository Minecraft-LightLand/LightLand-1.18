package dev.hikarishima.lightland.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.common.gui.ability.AbilityScreen;
import dev.hikarishima.lightland.content.magic.gui.hex.HexStatus;
import dev.hikarishima.lightland.content.magic.products.info.ProductState;
import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.magic.HexDirection;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

public class LangData {

    public enum IDS {
        INVALID_ID("argument.invalid_id", 0),
        PROF_EXIST("chat.profession_already_exist", 0),
        LOCKED("chat.locked", 0),
        UNLOCKED("chat.unlocked", 0),
        GET_ARCANE_MANA("chat.show_arcane_mana", 2),
        SPELL_SLOT("chat.spell_slot", 1),
        ACTION_SUCCESS("chat.action_success", 0),
        PLAYER_NOT_FOUND("chat.player_not_found", 0),
        WRONG_ITEM("chat.wrong_item", 0),
        TARGET_ALLY("tooltip.target.ally", 0),
        TARGET_ENEMY("tooltip.target.enemy", 0),
        TARGET_ALL("tooltip.target.all", 0),
        POTION_RADIUS("tooltip.potion.radius", 1),
        CONT_RITUAL("container.ritual", 0),
        BACKPACK_SLOT("tooltip.backpack_slot", 2),
        MANA_COST("tooltip.mana_cost", 1),

        LVUP_NO_POINT("screen.ability.ability.error.no_point", 0),
        LVUP_NO_PROF("screen.ability.ability.error.prof", 0),
        LVUP_MAX_REACHED("screen.ability.ability.error.max_reached", 0),
        LVUP_PROF_MAX("screen.ability.ability.error.prof_max", 0),

        HEX_COST("screen.hex.cost", 1),
        GUI_TREE_ELEM_PRE("screen.magic_tree.elem.require", 0),
        GUI_TREE_ELEM_POST("screen.magic_tree.elem.lv", 1),
        GUI_TREE_COST("screen.magic_tree.cost", 1),
        GUI_TREE_SHORT("screen.magic_tree.short", 0),
        GUI_TREE_REPEAT("screen.magic_tree.repeat", 0),
        GUI_ABILITY("screen.ability.ability.title", 0),
        GUI_ABILITY_LV("screen.ability.ability.desc.lv", 1),
        GUI_ABILITY_COST("screen.ability.ability.desc.cost", 2),
        GUI_ARCANE("screen.ability.arcane.title", 0),
        GUI_ARCANE_COST("screen.ability.arcane.cost", 2),
        GUI_ELEMENT("screen.ability.elemental.title", 0),
        GUI_ELEMENT_LV("screen.ability.elemental.desc.lv", 1),
        GUI_ELEMENT_COUNT("screen.ability.elemental.desc.count", 1),
        GUI_ELEMENT_COST("screen.ability.elemental.desc.cost", 2),
        GUI_PROF("screen.ability.profession.title", 0),
        GUI_PROF_EXIST("screen.ability.profession.desc.prof_already_exist", 0);

        final String id;
        final int count;

        IDS(String id, int count) {
            this.id = id;
            this.count = count;
        }

        public TranslatableComponent get(Object... objs) {
            if (objs.length != count)
                throw new IllegalArgumentException("for " + name() + ": expect " + count + " parameters, got " + objs.length);
            return new TranslatableComponent(LightLand.MODID + "." + id, objs);
        }

    }

    public enum Keys {
        SKILL_1("key.lightland.skill_1", GLFW.GLFW_KEY_Z),
        SKILL_2("key.lightland.skill_2", GLFW.GLFW_KEY_X),
        SKILL_3("key.lightland.skill_3", GLFW.GLFW_KEY_C),
        SKILL_4("key.lightland.skill_4", GLFW.GLFW_KEY_V);

        public final String id;
        public final int key;
        public final KeyMapping map;

        Keys(String id, int key) {
            this.id = id;
            this.key = key;
            map = new KeyMapping(id, key, "key.categories.lightland");
        }

    }

    public static final Map<Class<? extends Enum<?>>, String> MAP = new HashMap<>();

    static {
        MAP.put(HexDirection.class, "screen.hex.direction.");
        MAP.put(HexStatus.Compile.class, "screen.hex.compile.");
        MAP.put(HexStatus.Save.class, "screen.hex.save.");
        MAP.put(AbilityScreen.AbilityType.class, "screen.ability.ability.");
        MAP.put(ArcaneType.Hit.class, "screen.ability.arcane.activate.");
        MAP.put(ProductState.class, "screen.magic_tree.status.");
    }

    public static TranslatableComponent get(Enum<?> obj) {
        if (!MAP.containsKey(obj.getClass()))
            return new TranslatableComponent("unknown.enum." + obj.name().toLowerCase(Locale.ROOT));
        return new TranslatableComponent(MAP.get(obj.getClass()) + obj.name().toLowerCase(Locale.ROOT));
    }

    public static void addTranslations(BiConsumer<String, String> pvd) {
        for (IDS id : IDS.values()) {
            String[] strs = id.id.split("\\.");
            String str = strs[strs.length - 1];
            StringBuilder pad = new StringBuilder();
            for (int i = 0; i < id.count; i++) {
                pad.append(pad.length() == 0 ? ": " : "/");
                pad.append("%s");
            }
            pvd.accept(LightLand.MODID + "." + id.id, RegistrateLangProvider.toEnglishName(str) + pad);
        }
        for (Keys key : Keys.values()) {
            String[] strs = key.id.split("\\.");
            String str = strs[strs.length - 1];
            pvd.accept(key.id, RegistrateLangProvider.toEnglishName(str));
        }
        pvd.accept("itemGroup.lightland", "Light Land RPG");
        pvd.accept("key.categories.lightland", "Light Land Keys");
        MAP.forEach((v, k) -> {
            for (Enum<?> e : v.getEnumConstants()) {
                String en = e.name().toLowerCase(Locale.ROOT);
                pvd.accept(k + en, RegistrateLangProvider.toEnglishName(en));
            }
        });
    }
}
