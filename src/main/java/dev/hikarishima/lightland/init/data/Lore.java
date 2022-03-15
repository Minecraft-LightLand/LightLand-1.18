package dev.hikarishima.lightland.init.data;

import net.minecraft.network.chat.TranslatableComponent;

public enum Lore {
	ENCHANT_LOAD("enchant_load", "This piece of armor is over-enchanted. Wearers will be cursed");

	final String id;
	final String lore;

	Lore(String id, String lore) {
		this.id = id;
		this.lore = lore;
	}

	public TranslatableComponent get() {
		return new TranslatableComponent("lore.lightland." + id);
	}

}
