package dev.xkmc.cuisine.init.data;

import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateLangProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;
import java.util.Locale;

public enum LangData {
	TOOLTIP_FLUID_TITLE("tooltip.fluid.title", "Fluid:", 0, ChatFormatting.GRAY),
	TOOLTIP_FLUID_AMOUNT("tooltip.fluid.amount", "Amount: %s/%s mB", 2, null),
	TOOLTIP_FLUID_USES("tooltip.fluid.uses", "Amount: %s/%s uses", 2, null);

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;


	LangData(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = Cuisine.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		TranslatableComponent ans = new TranslatableComponent(key, args);
		if (format != null) {
			return ans.withStyle(format);
		}
		return ans;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (LangData lang : LangData.values()) {
			pvd.add(lang.key, lang.def);
		}
		pvd.add("itemGroup." + Cuisine.MODID + ".cuisine", "Cuisine");
	}

}
