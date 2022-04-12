package dev.xkmc.cuisine.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.cuisine.content.tools.jar.JarRecipeBuilder;
import dev.xkmc.cuisine.content.tools.pan.SaucePanRecipeBuilder;
import dev.xkmc.cuisine.init.registrate.SimpleItem;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		{
			unlock(pvd, new SaucePanRecipeBuilder(Items.RABBIT_STEW, 1, 100, Items.BOWL)::unlockedBy, Items.RABBIT_STEW)
					.requires(new FluidStack(Fluids.WATER, 2)).requires(Items.BOWL).requires(Items.RABBIT)
					.requires(Items.POTATO).requires(Items.CARROT).requires(Items.RED_MUSHROOM).requires(Items.BROWN_MUSHROOM)
					.save(pvd);
			unlock(pvd, new SaucePanRecipeBuilder(Items.MUSHROOM_STEW, 1, 100, Items.BOWL)::unlockedBy, Items.MUSHROOM_STEW)
					.requires(new FluidStack(Fluids.WATER, 2)).requires(Items.BOWL)
					.requires(Items.RED_MUSHROOM).requires(Items.BROWN_MUSHROOM)
					.save(pvd);
		}
		{
			unlock(pvd, new JarRecipeBuilder(SimpleItem.SALT.item.get(), 1, 100,
							new FluidStack(Fluids.WATER, 1000), FluidStack.EMPTY)::unlockedBy,
					SimpleItem.SALT.item.get()).save(pvd);
		}
	}

	private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
