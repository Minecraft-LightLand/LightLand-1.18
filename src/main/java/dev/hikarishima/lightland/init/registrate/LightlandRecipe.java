package dev.hikarishima.lightland.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.hikarishima.lightland.content.berserker.recipe.MedArmorRecipe;
import dev.hikarishima.lightland.content.common.recipe.BackpackDyeRecipe;
import dev.hikarishima.lightland.content.common.recipe.BackpackUpgradeRecipe;
import dev.hikarishima.lightland.content.secondary.pan.PanBlockEntity;
import dev.hikarishima.lightland.content.secondary.pan.SaucePanRecipe;
import dev.hikarishima.lightland.content.magic.block.RitualCore;
import dev.hikarishima.lightland.content.magic.products.recipe.DefMagicRecipe;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.content.magic.ritual.*;
import dev.lcy0x1.recipe.BaseRecipe;
import dev.lcy0x1.recipe.AbstractShapedRecipe;
import dev.lcy0x1.recipe.AbstractShapelessRecipe;
import dev.lcy0x1.recipe.AbstractSmithingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

public class LightlandRecipe {

	public static RecipeType<AbstractRitualRecipe<?>> RT_RITUAL;
	public static RecipeType<IMagicRecipe<?>> RT_MAGIC;
	public static RecipeType<SaucePanRecipe> RT_PAN;

	public static final RegistryEntry<BaseRecipe.RecType<BasicRitualRecipe, AbstractRitualRecipe<?>, RitualCore.Inv>> RS_DEF =
			REGISTRATE.simple("ritual_default", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(BasicRitualRecipe.class, RT_RITUAL));

	public static final RegistryEntry<BaseRecipe.RecType<EnchantRitualRecipe, AbstractRitualRecipe<?>, RitualCore.Inv>> RS_ENCH =
			REGISTRATE.simple("ritual_enchantment", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(EnchantRitualRecipe.class, RT_RITUAL));

	public static final RegistryEntry<BaseRecipe.RecType<PotionBoostRecipe, AbstractRitualRecipe<?>, RitualCore.Inv>> RSP_BOOST =
			REGISTRATE.simple("ritual_potion_boost", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PotionBoostRecipe.class, RT_RITUAL));
	public static final RegistryEntry<BaseRecipe.RecType<PotionCoreRecipe, AbstractRitualRecipe<?>, RitualCore.Inv>> RSP_CORE =
			REGISTRATE.simple("ritual_potion_core", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PotionCoreRecipe.class, RT_RITUAL));
	public static final RegistryEntry<BaseRecipe.RecType<PotionSpellRecipe, AbstractRitualRecipe<?>, RitualCore.Inv>> RSP_SPELL =
			REGISTRATE.simple("ritual_potion_spell", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PotionSpellRecipe.class, RT_RITUAL));
	public static final RegistryEntry<BaseRecipe.RecType<PotionModifyRecipe, AbstractRitualRecipe<?>, RitualCore.Inv>> RSP_MODIFY =
			REGISTRATE.simple("ritual_potion_modify", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PotionModifyRecipe.class, RT_RITUAL));

	public static final RegistryEntry<AbstractShapedRecipe.Serializer<MedArmorRecipe>> RSC_MED_ARMOR =
			REGISTRATE.simple("medicine_armor", RecipeSerializer.class, () -> new AbstractShapedRecipe.Serializer<>(MedArmorRecipe::new));
	public static final RegistryEntry<AbstractShapelessRecipe.Serializer<BackpackDyeRecipe>> RSC_BAG_DYE =
			REGISTRATE.simple("backpack_dye", RecipeSerializer.class, () -> new AbstractShapelessRecipe.Serializer<>(BackpackDyeRecipe::new));
	public static final RegistryEntry<AbstractSmithingRecipe.Serializer<BackpackUpgradeRecipe>> RSC_BAG_UPGRADE =
			REGISTRATE.simple("backpack_upgrade", RecipeSerializer.class, () -> new AbstractSmithingRecipe.Serializer<>(BackpackUpgradeRecipe::new));

	public static final RegistryEntry<BaseRecipe.RecType<DefMagicRecipe, IMagicRecipe<?>, IMagicRecipe.Inv>> RSM_DEF =
			REGISTRATE.simple("magic_default", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(DefMagicRecipe.class, RT_MAGIC));

	public static final RegistryEntry<BaseRecipe.RecType<SaucePanRecipe, SaucePanRecipe, PanBlockEntity.RecipeContainer>> RS_PAN =
			REGISTRATE.simple("sauce_pan", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(SaucePanRecipe.class, RT_PAN));

	public static void registerRecipeType() {
		RT_RITUAL = RecipeType.register("lightland:ritual");
		RT_MAGIC = RecipeType.register("lightland:magic");
		RT_PAN = RecipeType.register("lightland:sauce_pan");
	}

	public static void register() {

	}

}