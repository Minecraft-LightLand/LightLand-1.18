package dev.xkmc.cuisine.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.cuisine.content.tools.basin.BasinDryRecipeBuilder;
import dev.xkmc.cuisine.content.tools.basin.BasinRecipeBuilder;
import dev.xkmc.cuisine.content.tools.jar.JarRecipeBuilder;
import dev.xkmc.cuisine.content.tools.mill.MillRecipeBuilder;
import dev.xkmc.cuisine.content.tools.mortar.MortarRecipeBuilder;
import dev.xkmc.cuisine.content.tools.pan.PanRecipeBuilder;
import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.init.registrate.CuisineFluids;
import dev.xkmc.cuisine.init.registrate.SimpleItem;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class RecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		// woods
		{
			for (WoodType type : WoodType.values()) {
				pvd.planks(DataIngredient.items(type.LOG.get()), type.PLANK);
				pvd.slab(DataIngredient.items(type.PLANK.get()), type.SLAB, null, false);
				pvd.stairs(DataIngredient.items(type.PLANK.get()), type.STAIR, null, false);
				pvd.fence(DataIngredient.items(type.PLANK.get()), type.FENCE, null);
				pvd.fenceGate(DataIngredient.items(type.PLANK.get()), type.FENCE_GATE, null);
				pvd.trapDoor(DataIngredient.items(type.PLANK.get()), type.TRAPDOOR, null);
				pvd.door(DataIngredient.items(type.PLANK.get()), type.DOOR, null);
				pvd.singleItem(DataIngredient.items(type.PLANK.get()), type.BUTTON, 1, 1);
				boat(pvd, DataIngredient.items(type.PLANK.get()), type.BOAT_ITEM);
				pressurePlate(pvd, DataIngredient.items(type.PLANK.get()), type.PRESSURE_PLATE);
			}
		}
		// pan
		{
			unlock(pvd, new PanRecipeBuilder(Items.RABBIT_STEW, 1, 100)::unlockedBy, Items.RABBIT_STEW)
					.requires(new FluidStack(Fluids.WATER, 100)).requires(Items.RABBIT)
					.requires(Items.POTATO).requires(Items.CARROT).requires(Items.RED_MUSHROOM).requires(Items.BROWN_MUSHROOM)
					.save(pvd);
			unlock(pvd, new PanRecipeBuilder(Items.MUSHROOM_STEW, 1, 100)::unlockedBy, Items.MUSHROOM_STEW)
					.requires(new FluidStack(Fluids.WATER, 100))
					.requires(Items.RED_MUSHROOM).requires(Items.BROWN_MUSHROOM)
					.save(pvd);

			unlock(pvd, new PanRecipeBuilder(Items.HONEY_BOTTLE, 3, 100)::unlockedBy, Items.HONEYCOMB)
					.requires(new FluidStack(CuisineFluids.WATER.fluid.get(), 50))
					.requires(Items.BEEF)
					.save(pvd);
			unlock(pvd, new PanRecipeBuilder(Items.COOKED_PORKCHOP, 3, 100)::unlockedBy, Items.PORKCHOP)
					.requires(new FluidStack(CuisineFluids.EDIBLE_OIL.fluid.get(), 50))
					.requires(Items.PORKCHOP)
					.save(pvd);
		}
		// jar
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
			unlock(pvd, new JarRecipeBuilder(Items.SUGAR, 1, 100,
							new FluidStack(Fluids.WATER, 1000), FluidStack.EMPTY)::unlockedBy,
					SimpleItem.UNREFINED_SUGAR.item.get())
					.requires(SimpleItem.UNREFINED_SUGAR.item.get())
					.save(pvd, new ResourceLocation(Cuisine.MODID, "jar/sugar"));
		}
		// basin
		{
			unlock(pvd, new BasinRecipeBuilder(Ingredient.of(Items.SUGAR_CANE),
					new FluidStack(CuisineFluids.SUGARCANE_JUICE.fluid.get(), 250),
					5)::unlockedBy, Items.SUGAR_CANE)
					.save(pvd, new ResourceLocation(Cuisine.MODID, "basin/sugarcane_juice"));
			unlock(pvd, new BasinDryRecipeBuilder(new FluidStack(CuisineFluids.SUGARCANE_JUICE.fluid.get(), 250),
					SimpleItem.UNREFINED_SUGAR.item.asStack(1), 100)::unlockedBy, Items.SUGAR_CANE)
					.save(pvd, new ResourceLocation(Cuisine.MODID, "basin_dry/unrefined_sugar"));
		}
		// mill
		{
			unlock(pvd, new MillRecipeBuilder(Ingredient.of(CuisineCropType.SOYBEAN.getSeed()),
							new FluidStack(CuisineFluids.SOY_MILK.fluid.get(), 50), 50, 4)::unlockedBy,
					CuisineCropType.SOYBEAN.getSeed())
					.save(pvd, new ResourceLocation(Cuisine.MODID, "mill/soy_milk"));
		}
		// mortar
		{
			unlock(pvd, new MortarRecipeBuilder(Ingredient.of(CuisineCropType.CHILI.getSeed()),
							SimpleItem.CHILI_POWDER.item.asStack(), 5)::unlockedBy,
					CuisineCropType.CHILI.getSeed()).save(pvd);
			unlock(pvd, new MortarRecipeBuilder(Ingredient.of(CuisineCropType.SICHUAN_PEPPER.getSeed()),
							SimpleItem.SICHUAN_PEPPER_POWDER.item.asStack(), 5)::unlockedBy,
					CuisineCropType.SICHUAN_PEPPER.getSeed()).save(pvd);
			unlock(pvd, new MortarRecipeBuilder(Ingredient.of(CuisineCropType.RICE.getSeed()),
							SimpleItem.WHITE_RICE.item.asStack(), 5)::unlockedBy,
					CuisineCropType.RICE.getSeed()).save(pvd);
			unlock(pvd, new MortarRecipeBuilder(Ingredient.of(SimpleItem.WHITE_RICE.item.get()),
							SimpleItem.RICE_POWDER.item.asStack(), 5)::unlockedBy,
					SimpleItem.WHITE_RICE.item.get()).save(pvd);
		}
	}

	private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

	public static <T extends ItemLike & IForgeRegistryEntry<?>> void boat(RegistrateRecipeProvider pvd, DataIngredient source, Supplier<? extends T> result) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("X X").pattern("XXX")
				.define('X', source)
				.unlockedBy("has_" + pvd.safeName(source), source.getCritereon(pvd))
				.save(pvd, pvd.safeId(result.get()));
	}

	public static <T extends ItemLike & IForgeRegistryEntry<?>> void pressurePlate(RegistrateRecipeProvider pvd, DataIngredient source, Supplier<? extends T> result) {
		ShapedRecipeBuilder.shaped(result.get())
				.pattern("XX")
				.define('X', source)
				.unlockedBy("has_" + pvd.safeName(source), source.getCritereon(pvd))
				.save(pvd, pvd.safeId(result.get()));
	}

}
