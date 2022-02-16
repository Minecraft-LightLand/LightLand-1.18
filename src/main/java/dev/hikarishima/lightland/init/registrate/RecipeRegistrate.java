package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.hikarishima.lightland.content.magic.ritual.*;
import dev.lcy0x1.base.BaseRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

public class RecipeRegistrate {

    public static final RecipeType<AbstractMagicRitualRecipe<?>> RT_RITUAL = RecipeType.register("lightland:ritual");

    public static final RegistryEntry<BaseRecipe.RecType<BasicMagicRitualRecipe, AbstractMagicRitualRecipe<?>, RitualCore.Inv>> RS_DEF =
            REGISTRATE.simple(RecipeSerializer.class, () -> new BaseRecipe.RecType<>(BasicMagicRitualRecipe.class, RT_RITUAL));

    public static final RegistryEntry<BaseRecipe.RecType<EnchantMagicRitualRecipe, AbstractMagicRitualRecipe<?>, RitualCore.Inv>> RS_ENCH =
            REGISTRATE.simple(RecipeSerializer.class, () -> new BaseRecipe.RecType<>(EnchantMagicRitualRecipe.class, RT_RITUAL));

    public static final RegistryEntry<BaseRecipe.RecType<PotionBoostRecipe, AbstractMagicRitualRecipe<?>, RitualCore.Inv>> RSP_BOOST =
            REGISTRATE.simple(RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PotionBoostRecipe.class, RT_RITUAL));
    public static final RegistryEntry<BaseRecipe.RecType<PotionCoreRecipe, AbstractMagicRitualRecipe<?>, RitualCore.Inv>> RSP_CORE =
            REGISTRATE.simple(RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PotionCoreRecipe.class, RT_RITUAL));
    public static final RegistryEntry<BaseRecipe.RecType<PotionSpellRecipe, AbstractMagicRitualRecipe<?>, RitualCore.Inv>> RSP_SPELL =
            REGISTRATE.simple(RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PotionSpellRecipe.class, RT_RITUAL));
    public static final RegistryEntry<BaseRecipe.RecType<PotionModifyRecipe, AbstractMagicRitualRecipe<?>, RitualCore.Inv>> RSP_MODIFY =
            REGISTRATE.simple(RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PotionModifyRecipe.class, RT_RITUAL));

    public static void register() {

    }

}
