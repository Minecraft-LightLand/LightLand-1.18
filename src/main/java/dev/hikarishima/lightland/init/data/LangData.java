package dev.hikarishima.lightland.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.network.chat.TranslatableComponent;

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
        CONT_RITUAL("container.ritual", 0);

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
        pvd.accept("itemGroup.lightland", "Light Land RPG");
    }
}
