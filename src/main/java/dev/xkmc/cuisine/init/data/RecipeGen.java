package dev.xkmc.cuisine.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.cuisine.content.tools.jar.JarRecipeBuilder;
import dev.xkmc.cuisine.content.tools.pan.PanRecipeBuilder;
import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.init.registrate.CuisineFluids;
import dev.xkmc.cuisine.init.registrate.SimpleItem;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		{
			unlock(pvd, new PanRecipeBuilder(Items.RABBIT_STEW, 1, 100, Items.BOWL)::unlockedBy, Items.RABBIT_STEW)
					.requires(new FluidStack(Fluids.WATER, 2)).requires(Items.BOWL).requires(Items.RABBIT)
					.requires(Items.POTATO).requires(Items.CARROT).requires(Items.RED_MUSHROOM).requires(Items.BROWN_MUSHROOM)
					.save(pvd);
			unlock(pvd, new PanRecipeBuilder(Items.MUSHROOM_STEW, 1, 100, Items.BOWL)::unlockedBy, Items.MUSHROOM_STEW)
					.requires(new FluidStack(Fluids.WATER, 2)).requires(Items.BOWL)
					.requires(Items.RED_MUSHROOM).requires(Items.BROWN_MUSHROOM)
					.save(pvd);
		}
		{
			unlock(pvd, new JarRecipeBuilder(Items.AIR, 0, 100, new FluidStack(Fluids.WATER, 1000),
							new FluidStack(CuisineFluids.SOY_SAUCE.fluid.get(), 250))::unlockedBy,
					SimpleItem.SALT.item.get())
					.requires(SimpleItem.SALT.item.get())
					.requires(CuisineCropType.SOYBEAN.getSeed())
					.save(pvd, new ResourceLocation(Cuisine.MODID, "jar/soy_sauce"));
			unlock(pvd, new JarRecipeBuilder(Items.AIR, 0, 100, new FluidStack(Fluids.WATER, 1000),
							new FluidStack(CuisineFluids.FRUIT_VINEGAR.fluid.get(), 250))::unlockedBy,
					Items.APPLE)
					.requires(CuisineTags.AllItemTags.FRUIT.tag)
					.save(pvd);
			unlock(pvd, new JarRecipeBuilder(SimpleItem.UNREFINED_SUGAR.item.get(), 1, 100,
							new FluidStack(CuisineFluids.SUGARCANE_JUICE.fluid.get(), 250),
							FluidStack.EMPTY)::unlockedBy,
					Items.SUGAR_CANE)
					.save(pvd, new ResourceLocation(Cuisine.MODID, "jar/unrefined_sugar"));
			unlock(pvd, new JarRecipeBuilder(Items.SUGAR, 1, 100,
							new FluidStack(Fluids.WATER, 1000), FluidStack.EMPTY)::unlockedBy,
					SimpleItem.UNREFINED_SUGAR.item.get())
					.requires(SimpleItem.UNREFINED_SUGAR.item.get())
					.save(pvd, new ResourceLocation(Cuisine.MODID, "jar/sugar"));
		}
	}

	private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
