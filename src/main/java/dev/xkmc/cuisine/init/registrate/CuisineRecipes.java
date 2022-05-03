package dev.xkmc.cuisine.init.registrate;

import dev.xkmc.l2library.recipe.BaseRecipe;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.basin.BasinBlockEntity;
import dev.xkmc.cuisine.content.tools.basin.BasinDryRecipe;
import dev.xkmc.cuisine.content.tools.basin.BasinRecipe;
import dev.xkmc.cuisine.content.tools.firepit.stick.FirePitStickBlockEntity;
import dev.xkmc.cuisine.content.tools.firepit.stick.FirePitStickRecipe;
import dev.xkmc.cuisine.content.tools.firepit.wok.FirePitWokBlockEntity;
import dev.xkmc.cuisine.content.tools.firepit.wok.FirePitWokRecipe;
import dev.xkmc.cuisine.content.tools.jar.JarBlockEntity;
import dev.xkmc.cuisine.content.tools.jar.JarRecipe;
import dev.xkmc.cuisine.content.tools.mill.MillBlockEntity;
import dev.xkmc.cuisine.content.tools.mill.MillRecipe;
import dev.xkmc.cuisine.content.tools.mortar.MortarBlockEntity;
import dev.xkmc.cuisine.content.tools.mortar.MortarRecipe;
import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import dev.xkmc.cuisine.content.tools.pan.PanRecipe;
import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public class CuisineRecipes {

	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, Cuisine.MODID);

	public static RegistryObject<RecipeType<PanRecipe>> RT_PAN = REGISTRATE.recipe(RECIPE_TYPES, "pan");
	public static RegistryObject<RecipeType<JarRecipe>> RT_JAR = REGISTRATE.recipe(RECIPE_TYPES, "jar");
	public static RegistryObject<RecipeType<BasinRecipe>> RT_BASIN = REGISTRATE.recipe(RECIPE_TYPES, "basin");
	public static RegistryObject<RecipeType<BasinDryRecipe>> RT_BASIN_DRY = REGISTRATE.recipe(RECIPE_TYPES, "basin_dry");
	public static RegistryObject<RecipeType<MillRecipe>> RT_MILL = REGISTRATE.recipe(RECIPE_TYPES, "mill");
	public static RegistryObject<RecipeType<MortarRecipe>> RT_MORTAR = REGISTRATE.recipe(RECIPE_TYPES, "mortar");
	public static RegistryObject<RecipeType<FirePitStickRecipe>> RT_STICK = REGISTRATE.recipe(RECIPE_TYPES, "stick");
	public static RegistryObject<RecipeType<FirePitWokRecipe>> RT_WOK = REGISTRATE.recipe(RECIPE_TYPES, "wok");

	public static final RegistryEntry<BaseRecipe.RecType<PanRecipe, PanRecipe, RecipeContainer<PanBlockEntity>>> RS_PAN =
			REGISTRATE.simple("pan", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PanRecipe.class, RT_PAN));
	public static final RegistryEntry<BaseRecipe.RecType<JarRecipe, JarRecipe, RecipeContainer<JarBlockEntity>>> RS_JAR =
			REGISTRATE.simple("jar", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(JarRecipe.class, RT_JAR));
	public static final RegistryEntry<BaseRecipe.RecType<BasinRecipe, BasinRecipe, RecipeContainer<BasinBlockEntity>>> RS_BASIN =
			REGISTRATE.simple("basin", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(BasinRecipe.class, RT_BASIN));
	public static final RegistryEntry<BaseRecipe.RecType<BasinDryRecipe, BasinDryRecipe, RecipeContainer<BasinBlockEntity>>> RS_BASIN_DRY =
			REGISTRATE.simple("basin_dry", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(BasinDryRecipe.class, RT_BASIN_DRY));
	public static final RegistryEntry<BaseRecipe.RecType<MillRecipe, MillRecipe, RecipeContainer<MillBlockEntity>>> RS_MILL =
			REGISTRATE.simple("mill", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(MillRecipe.class, RT_MILL));
	public static final RegistryEntry<BaseRecipe.RecType<MortarRecipe, MortarRecipe, RecipeContainer<MortarBlockEntity>>> RS_MORTAR =
			REGISTRATE.simple("mortar", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(MortarRecipe.class, RT_MORTAR));
	public static final RegistryEntry<BaseRecipe.RecType<FirePitStickRecipe, FirePitStickRecipe, RecipeContainer<FirePitStickBlockEntity>>> RS_STICK =
			REGISTRATE.simple("stick", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(FirePitStickRecipe.class, RT_STICK));
	public static final RegistryEntry<BaseRecipe.RecType<FirePitWokRecipe, FirePitWokRecipe, RecipeContainer<FirePitWokBlockEntity>>> RS_WOK =
			REGISTRATE.simple("wok", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(FirePitWokRecipe.class, RT_WOK));

	public static void register(IEventBus bus) {
		RECIPE_TYPES.register(bus);
	}

}
