package dev.xkmc.lightland.init.registrate;

import dev.xkmc.l2library.recipe.AbstractShapedRecipe;
import dev.xkmc.l2library.recipe.BaseRecipe;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.lightland.content.berserker.recipe.MedArmorRecipe;
import dev.xkmc.lightland.content.magic.block.RitualCore;
import dev.xkmc.lightland.content.magic.products.recipe.DefMagicRecipe;
import dev.xkmc.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.xkmc.lightland.content.magic.ritual.*;
import dev.xkmc.lightland.init.LightLand;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static dev.xkmc.lightland.init.LightLand.REGISTRATE;

public class LightlandRecipe {

	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, LightLand.MODID);
	public static RegistryObject<RecipeType<AbstractRitualRecipe<?>>> RT_RITUAL = REGISTRATE.recipe(RECIPE_TYPES, "ritual");
	public static RegistryObject<RecipeType<IMagicRecipe<?>>> RT_MAGIC = REGISTRATE.recipe(RECIPE_TYPES, "magic");

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

	public static final RegistryEntry<BaseRecipe.RecType<DefMagicRecipe, IMagicRecipe<?>, IMagicRecipe.Inv>> RSM_DEF =
			REGISTRATE.simple("magic_default", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(DefMagicRecipe.class, RT_MAGIC));

	public static void register(IEventBus bus) {
		RECIPE_TYPES.register(bus);
	}

}
