package dev.hikarishima.lightland.init.special;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.magic.spell.Spell;
import dev.hikarishima.lightland.content.profession.*;
import dev.hikarishima.lightland.content.skill.Skill;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class LightLandRegistry {

    public static IForgeRegistry<ArcaneType> ARCANE_TYPE;
    public static IForgeRegistry<Arcane> ARCANE;
    public static IForgeRegistry<Spell<?, ?>> SPELL;
    public static IForgeRegistry<Profession> PROFESSION;
    public static IForgeRegistry<Skill> SKILL;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void createRegistries() {
        //ELEMENT = new RegistryBuilder<MagicElement>().setName(new ResourceLocation(LightLandMagic.MODID, "magic_element")).setType(MagicElement.class).create();

        //PRODUCT_TYPE = new RegistryBuilder().setName(new ResourceLocation(LightLandMagic.MODID, "magic_product_type")).setType(MagicProductType.class).create();

        ARCANE_TYPE = new RegistryBuilder<ArcaneType>()
                .setName(new ResourceLocation(LightLand.MODID, "arcane_type"))
                .setType(ArcaneType.class).create();

        ARCANE = new RegistryBuilder<Arcane>()
                .setName(new ResourceLocation(LightLand.MODID, "arcane"))
                .setType(Arcane.class).create();

        SPELL = new RegistryBuilder().setName(new ResourceLocation(LightLand.MODID, "spell")).setType(Spell.class).create();

        PROFESSION = new RegistryBuilder<Profession>()
                .setName(new ResourceLocation(LightLand.MODID, "profession"))
                .setType(Profession.class).create();

        SKILL = new RegistryBuilder<Skill>().setName(new ResourceLocation(LightLand.MODID, "skill")).setType(Skill.class).create();

        ArcaneType.register();
        ArcaneRegistry.register();
    }

    public static final RegistryEntry<ArcaneProfession> PROF_ARCANE = genProf("arcane", ArcaneProfession::new);
    public static final RegistryEntry<MagicianProfession> PROF_MAGIC = genProf("magician", MagicianProfession::new);
    public static final RegistryEntry<SpellCasterProfession> PROF_SPELL = genProf("spell_caster", SpellCasterProfession::new);
    public static final RegistryEntry<KnightProfession> PROF_KNIGHT = genProf("knight", KnightProfession::new);
    public static final RegistryEntry<ShielderProfession> PROF_SHIELDER = genProf("shielder", ShielderProfession::new);
    public static final RegistryEntry<BurserkerProfession> PROF_BURSERKER = genProf("burserker", BurserkerProfession::new);
    public static final RegistryEntry<ArcherProfession> PROF_ARCHER = genProf("archer", ArcherProfession::new);
    public static final RegistryEntry<HunterProfession> PROF_HUNTER = genProf("hunter", HunterProfession::new);
    public static final RegistryEntry<AlchemistProfession> PROF_ALCHEM = genProf("alchemist", AlchemistProfession::new);
    public static final RegistryEntry<ChemistProfession> PROF_CHEM = genProf("chemist", ChemistProfession::new);
    public static final RegistryEntry<TidecallerProfession> PROF_TIDE = genProf("tidecaller", TidecallerProfession::new);
    public static final RegistryEntry<AssassinProfession> PROF_ASSASSIN = genProf("assassin", AssassinProfession::new);

    private static <V extends Profession> RegistryEntry<V> genProf(String name, NonNullSupplier<V> v) {
        return LightLand.REGISTRATE.generic(Profession.class, name, v).defaultLang().register();
    }

}
