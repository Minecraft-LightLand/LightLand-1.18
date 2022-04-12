package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.lcy0x1.recipe.BaseRecipe;
import dev.xkmc.cuisine.content.tools.basin.BasinBlockEntity;
import dev.xkmc.cuisine.content.tools.basin.BasinRecipe;
import dev.xkmc.cuisine.content.tools.jar.JarBlockEntity;
import dev.xkmc.cuisine.content.tools.jar.JarRecipe;
import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import dev.xkmc.cuisine.content.tools.pan.PanRecipe;
import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

public class CuisineRecipe {

	public static RecipeType<PanRecipe> RT_PAN;
	public static RecipeType<JarRecipe> RT_JAR;
	public static RecipeType<BasinRecipe> RT_BASIN;

	public static final RegistryEntry<BaseRecipe.RecType<PanRecipe, PanRecipe, PanBlockEntity.RecipeContainer>> RS_PAN =
			REGISTRATE.simple("pan", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(PanRecipe.class, RT_PAN));
	public static final RegistryEntry<BaseRecipe.RecType<JarRecipe, JarRecipe, JarBlockEntity.RecipeContainer>> RS_JAR =
			REGISTRATE.simple("jar", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(JarRecipe.class, RT_JAR));
	public static final RegistryEntry<BaseRecipe.RecType<BasinRecipe, BasinRecipe, BasinBlockEntity.RecipeContainer>> RS_BASIN =
			REGISTRATE.simple("basin", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(BasinRecipe.class, RT_BASIN));

	public static void registerRecipeType() {
		RT_PAN = RecipeType.register(Cuisine.MODID + ":pan");
		RT_JAR = RecipeType.register(Cuisine.MODID + ":jar");
		RT_BASIN = RecipeType.register(Cuisine.MODID + ":basin");
	}

	public static void register() {
	}

}
