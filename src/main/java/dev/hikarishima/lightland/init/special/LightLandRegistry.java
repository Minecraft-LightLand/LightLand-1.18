package dev.hikarishima.lightland.init.special;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.magic.products.MagicElement;
import dev.hikarishima.lightland.content.magic.products.MagicProductType;
import dev.hikarishima.lightland.content.magic.spell.internal.Spell;
import dev.hikarishima.lightland.content.profession.Profession;
import dev.hikarishima.lightland.content.profession.prof.*;
import dev.hikarishima.lightland.content.skill.internal.Skill;
import dev.hikarishima.lightland.init.LightLand;
import dev.lcy0x1.util.Automator;
import dev.lcy0x1.util.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

public class LightLandRegistry {

	public static IForgeRegistry<MagicElement> ELEMENT;
	public static IForgeRegistry<MagicProductType<?, ?>> PRODUCT_TYPE;
	public static IForgeRegistry<ArcaneType> ARCANE_TYPE;
	public static IForgeRegistry<Arcane> ARCANE;
	public static IForgeRegistry<Spell<?, ?>> SPELL;
	public static IForgeRegistry<Profession> PROFESSION;
	public static IForgeRegistry<Skill<?, ?>> SKILL;

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void createRegistries(NewRegistryEvent event) {
		event.create(new RegistryBuilder<MagicElement>()
				.setName(new ResourceLocation(LightLand.MODID, "magic_element"))
				.setType(MagicElement.class), e -> ELEMENT = regSerializer(e));

		event.create(new RegistryBuilder()
				.setName(new ResourceLocation(LightLand.MODID, "magic_product_type"))
				.setType(MagicProductType.class), e -> PRODUCT_TYPE = regSerializer(e));

		event.create(new RegistryBuilder<ArcaneType>()
				.setName(new ResourceLocation(LightLand.MODID, "arcane_type"))
				.setType(ArcaneType.class), e -> ARCANE_TYPE = regSerializer(e));

		event.create(new RegistryBuilder<Arcane>()
				.setName(new ResourceLocation(LightLand.MODID, "arcane"))
				.setType(Arcane.class), e -> ARCANE = regSerializer(e));

		event.create(new RegistryBuilder()
				.setName(new ResourceLocation(LightLand.MODID, "spell"))
				.setType(Spell.class), e -> SPELL = regSerializer(e));

		event.create(new RegistryBuilder<Profession>()
				.setName(new ResourceLocation(LightLand.MODID, "profession"))
				.setType(Profession.class), e -> PROFESSION = regSerializer(e));

		event.create(new RegistryBuilder()
				.setName(new ResourceLocation(LightLand.MODID, "skill"))
				.setType(Skill.class), e -> SKILL = regSerializer(e));

		MagicRegistry.register();
		ArcaneType.register();
		ArcaneRegistry.register();
		SpellRegistry.register();
		SkillRegistry.register();
	}

	public static final RegistryEntry<ArcaneProfession> PROF_ARCANE = genProf("arcane", ArcaneProfession::new);
	public static final RegistryEntry<MagicianProfession> PROF_MAGIC = genProf("magician", MagicianProfession::new);
	public static final RegistryEntry<SpellCasterProfession> PROF_SPELL = genProf("spell_caster", SpellCasterProfession::new);
	public static final RegistryEntry<KnightProfession> PROF_KNIGHT = genProf("knight", KnightProfession::new);
	public static final RegistryEntry<ShielderProfession> PROF_SHIELDER = genProf("shielder", ShielderProfession::new);
	public static final RegistryEntry<BerserkerProfession> PROF_BURSERKER = genProf("berserker", BerserkerProfession::new);
	public static final RegistryEntry<ArcherProfession> PROF_ARCHER = genProf("archer", ArcherProfession::new);
	public static final RegistryEntry<HunterProfession> PROF_HUNTER = genProf("hunter", HunterProfession::new);
	public static final RegistryEntry<AlchemistProfession> PROF_ALCHEM = genProf("alchemist", AlchemistProfession::new);
	public static final RegistryEntry<ChemistProfession> PROF_CHEM = genProf("chemist", ChemistProfession::new);
	public static final RegistryEntry<TidecallerProfession> PROF_TIDE = genProf("tidecaller", TidecallerProfession::new);
	public static final RegistryEntry<AssassinProfession> PROF_ASSASSIN = genProf("assassin", AssassinProfession::new);

	private static <V extends Profession> RegistryEntry<V> genProf(String name, NonNullSupplier<V> v) {
		return LightLand.REGISTRATE.generic(Profession.class, name, v).defaultLang().register();
	}

	@SuppressWarnings({"rawtypes"})
	private static <T extends IForgeRegistryEntry<T>> IForgeRegistry regSerializer(IForgeRegistry<T> r) {
		new Serializer.RLClassHandler<>(r.getRegistrySuperType(), () -> r);
		new Automator.RegistryClassHandler<>(r.getRegistrySuperType(), () -> r);
		return r;
	}

}
