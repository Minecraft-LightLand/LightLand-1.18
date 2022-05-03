package dev.xkmc.lightland.init.special;

import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.lightland.content.arcane.internal.Arcane;
import dev.xkmc.lightland.content.magic.products.MagicElement;
import dev.xkmc.lightland.content.magic.products.MagicProductType;
import dev.xkmc.lightland.content.magic.products.instance.*;
import dev.xkmc.lightland.content.magic.spell.internal.Spell;
import dev.xkmc.lightland.init.LightLand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicRegistry {

	public static final RegistryEntry<MagicElement> ELEM_EARTH = regElem("earth", () -> new MagicElement(0x7F511F));
	public static final RegistryEntry<MagicElement> ELEM_WATER = regElem("water", () -> new MagicElement(0x008ED6));
	public static final RegistryEntry<MagicElement> ELEM_AIR = regElem("air", () -> new MagicElement(0xB2FF6B));
	public static final RegistryEntry<MagicElement> ELEM_FIRE = regElem("fire", () -> new MagicElement(0xFF3B21));
	public static final RegistryEntry<MagicElement> ELEM_QUINT = regElem("quint", () -> new MagicElement(0x9400FF));

	public static final RegistryEntry<MagicProductType<Enchantment, EnchantmentMagic>> MPT_ENCH =
			regProd("enchantment", () -> new MagicProductType<>(EnchantmentMagic.class, EnchantmentMagic::new,
					() -> ForgeRegistries.ENCHANTMENTS, Enchantment::getDescriptionId, ELEM_AIR.get()));
	public static final RegistryEntry<MagicProductType<MobEffect, PotionMagic>> MPT_EFF =
			regProd("effect", () -> new MagicProductType<>(PotionMagic.class, PotionMagic::new,
					() -> ForgeRegistries.MOB_EFFECTS, MobEffect::getDescriptionId, ELEM_WATER.get()));
	public static final RegistryEntry<MagicProductType<Arcane, ArcaneMagic>> MPT_ARCANE =
			regProd("arcane", () -> new MagicProductType<>(ArcaneMagic.class, ArcaneMagic::new,
					() -> LightLandRegistry.ARCANE, Arcane::getDescriptionId, ELEM_QUINT.get()));
	public static final RegistryEntry<MagicProductType<Spell<?, ?>, SpellMagic>> MPT_SPELL =
			regProd("spell", () -> new MagicProductType<>(SpellMagic.class, SpellMagic::new,
					() -> LightLandRegistry.SPELL, Spell::getDescriptionId, ELEM_FIRE.get()));
	public static final RegistryEntry<MagicProductType<Item, CraftMagic>> MPT_CRAFT =
			regProd("craft", () -> new MagicProductType<>(CraftMagic.class, CraftMagic::new,
					() -> ForgeRegistries.ITEMS, Item::getDescriptionId, ELEM_EARTH.get()));

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <T extends MagicProductType<?, ?>> RegistryEntry<T> regProd(String id, NonNullSupplier<T> sup) {
		return LightLand.REGISTRATE.generic((Class<MagicProductType<?, ?>>) (Class) MagicProductType.class, id, sup).defaultLang().register();
	}

	public static <T extends MagicElement> RegistryEntry<T> regElem(String id, NonNullSupplier<T> sup) {
		return LightLand.REGISTRATE.generic(MagicElement.class, id, sup).defaultLang().register();
	}

	public static void register() {
	}

}
