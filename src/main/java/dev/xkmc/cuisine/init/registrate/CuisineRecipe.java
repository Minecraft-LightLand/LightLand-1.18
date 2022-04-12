package dev.xkmc.cuisine.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.lcy0x1.recipe.BaseRecipe;
import dev.xkmc.cuisine.content.tools.jar.JarBlockEntity;
import dev.xkmc.cuisine.content.tools.jar.JarRecipe;
import dev.xkmc.cuisine.content.tools.pan.PanBlockEntity;
import dev.xkmc.cuisine.content.tools.pan.SaucePanRecipe;
import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import static dev.hikarishima.lightland.init.LightLand.REGISTRATE;

public class CuisineRecipe {

	public static RecipeType<SaucePanRecipe> RT_PAN;
	public static RecipeType<JarRecipe> RT_JAR;

	public static final RegistryEntry<BaseRecipe.RecType<SaucePanRecipe, SaucePanRecipe, PanBlockEntity.RecipeContainer>> RS_PAN =
			REGISTRATE.simple("sauce_pan", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(SaucePanRecipe.class, RT_PAN));
	public static final RegistryEntry<BaseRecipe.RecType<JarRecipe, JarRecipe, JarBlockEntity.RecipeContainer>> RS_JAR =
			REGISTRATE.simple("jar", RecipeSerializer.class, () -> new BaseRecipe.RecType<>(JarRecipe.class, RT_JAR));

	public static void registerRecipeType() {
		RT_PAN = RecipeType.register(Cuisine.MODID + ":sauce_pan");
		RT_JAR = RecipeType.register(Cuisine.MODID + ":jar");
	}

	public static void register() {
	}

}
