package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.lcy0x1.recipe.BaseRecipe;
import dev.xkmc.cuisine.content.tools.base.RecipeContainer;
import dev.xkmc.cuisine.content.tools.basin.BasinBlockEntity;
import dev.xkmc.cuisine.content.tools.basin.BasinDryRecipe;
import dev.xkmc.cuisine.content.tools.basin.BasinRecipe;
import dev.xkmc.cuisine.content.tools.firepit.FirePitStickBlockEntity;
import dev.xkmc.cuisine.content.tools.firepit.FirePitStickRecipe;
import dev.xkmc.cuisine.content.tools.jar.JarBlockEntity;
import dev.xkmc.cuisine.content.tools.jar.JarRecipe;
import dev.xkmc.cuisine.content.tools.mill.MillBlockEntity;
import dev.xkmc.cuisine.content.tools.mill.MillRecipe;
import dev.xkmc.cuisine.content.tools.mortar.MortarBlockEntity;
import dev.xkmc.cuisine.content.tools.mortar.MortarRecipe;
import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import dev.xkmc.cuisine.content.tools.pan.PanRecipe;
import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

public class CuisineRecipes {

	public static RecipeType<PanRecipe> RT_PAN;
	public static RecipeType<JarRecipe> RT_JAR;
	public static RecipeType<BasinRecipe> RT_BASIN;
	public static RecipeType<BasinDryRecipe> RT_BASIN_DRY;
	public static RecipeType<MillRecipe> RT_MILL;
	public static RecipeType<MortarRecipe> RT_MORTAR;
	public static RecipeType<FirePitStickRecipe> RT_STICK;

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

	public static void registerRecipeType() {
		RT_PAN = RecipeType.register(Cuisine.MODID + ":pan");
		RT_JAR = RecipeType.register(Cuisine.MODID + ":jar");
		RT_BASIN = RecipeType.register(Cuisine.MODID + ":basin");
		RT_BASIN_DRY = RecipeType.register(Cuisine.MODID + ":basin_dry");
		RT_MILL = RecipeType.register(Cuisine.MODID + ":mill");
		RT_MORTAR = RecipeType.register(Cuisine.MODID + ":mortar");
	}

	public static void register() {
	}

}
