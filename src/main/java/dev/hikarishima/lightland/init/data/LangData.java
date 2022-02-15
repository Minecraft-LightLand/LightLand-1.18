package dev.hikarishima.lightland.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.BiConsumer;

public class LangData {

    public enum IDS {
        INVALID_ID("argument.invalid_id"),
        PROF_EXIST("chat.profession_already_exist"),
        LOCKED("chat.locked"),
        UNLOCKED("chat.unlocked"),
        GET_ARCANE_MANA("chat.show_arcane_mana"),
        ACTION_SUCCESS("chat.action_success"),
        PLAYER_NOT_FOUND("chat.player_not_found"),
        WRONG_ITEM("chat.wrong_item"),
        TARGET_ALLY("tooltip.target.ally"),
        TARGET_ENEMY("tooltip.target.enemy"),
        TARGET_ALL("tooltip.target.all"),
        POTION_RADIUS("tooltip.potion.radius");

        final String id;

        IDS(String id) {
            this.id = id;
        }

        public TranslatableComponent get(Object... objs) {
            return new TranslatableComponent(LightLand.MODID + "." + id, objs);
        }

    }

    public static void addTranslations(BiConsumer<String, String> pvd) {
        for (IDS id : IDS.values()) {
            String[] strs = id.id.split("\\.");
            String str = strs[strs.length - 1];
            pvd.accept(LightLand.MODID + "." + id.id, RegistrateLangProvider.toEnglishName(str));
        }
    }
}
