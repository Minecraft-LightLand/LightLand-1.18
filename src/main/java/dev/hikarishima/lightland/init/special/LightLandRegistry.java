package dev.hikarishima.lightland.init.special;

import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.profession.*;
import dev.hikarishima.lightland.content.spell.internal.Spell;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class LightLandRegistry {

    public static IForgeRegistry<ArcaneType> ARCANE_TYPE;
    public static IForgeRegistry<Arcane> ARCANE;
    public static IForgeRegistry<Spell<?,?>> SPELL;
    public static IForgeRegistry<Profession> PROFESSION;

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

        //SKILL = new RegistryBuilder<Skill>().setName(new ResourceLocation(LightLand.MODID, "skill")).setType(Skill.class).create();

    }

    public static final ArcaneProfession PROF_ARCANE = reg("arcane", new ArcaneProfession());
    public static final MagicianProfession PROF_MAGIC = reg("magician", new MagicianProfession());
    public static final SpellCasterProfession PROF_SPELL = reg("spell_caster", new SpellCasterProfession());
    public static final KnightProfession PROF_KNIGHT = reg("knight", new KnightProfession());
    public static final ShielderProfession PROF_SHIELDER = reg("shielder", new ShielderProfession());
    public static final BurserkerProfession PROF_BURSERKER = reg("burserker", new BurserkerProfession());
    public static final ArcherProfession PROF_ARCHER = reg("archer", new ArcherProfession());
    public static final HunterProfession PROF_HUNTER = reg("hunter", new HunterProfession());
    public static final AlchemistProfession PROF_ALCHEM = reg("alchemist", new AlchemistProfession());
    public static final ChemistProfession PROF_CHEM = reg("chemist", new ChemistProfession());
    public static final TidecallerProfession PROF_TIDE = reg("tidecaller", new TidecallerProfession());
    public static final AssassinProfession PROF_ASSASSIN = reg("assassin", new AssassinProfession());

    private static <V extends T, T extends ForgeRegistryEntry<T>> V reg(String name, V v) {
        v.setRegistryName(LightLand.MODID, name);
        return v;
    }

}
