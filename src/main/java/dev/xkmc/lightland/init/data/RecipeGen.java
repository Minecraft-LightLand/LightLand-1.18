package dev.xkmc.lightland.init.data;

import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.repack.registrate.util.DataIngredient;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.lightland.init.registrate.LightlandBlocks;
import dev.xkmc.lightland.init.registrate.LightlandItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		{
			for (int i = 0; i < GenItem.Mats.values().length; i++) {
				genTools(pvd, i, Items.STICK);
			}
			storage(pvd, LightlandItems.ENC_GOLD_NUGGET, LightlandItems.ENC_GOLD_INGOT, LightlandBlocks.ENCHANT_GOLD_BLOCK);
			storage(pvd, LightlandItems.LEAD_NUGGET, LightlandItems.LEAD_INGOT, LightlandBlocks.LEAD_BLOCK);
			storage(pvd, LightlandItems.MAGICIUM_NUGGET, LightlandItems.MAGICIUM_INGOT, LightlandBlocks.MAGICIUM_BLOCK);
		}
	}

	private static void storage(RegistrateRecipeProvider pvd, ItemEntry<?> nugget, ItemEntry<?> ingot, BlockEntry<?> block) {
		pvd.storage(nugget, ingot);
		pvd.storage(ingot, block);
	}

	private static void genTools(RegistrateRecipeProvider pvd, int i, Item stick) {
		storage(pvd, LightlandItems.MAT_NUGGETS[i], LightlandItems.MAT_INGOTS[i], LightlandBlocks.GEN_BLOCK[i]);
		Item item = LightlandItems.MAT_INGOTS[i].get();
		ItemEntry<?>[] arr = LightlandItems.GEN_ITEM[i];
		unlock(pvd, new ShapedRecipeBuilder(arr[0].get(), 1)::unlockedBy, arr[0].get())
				.pattern("A A").pattern("A A").define('A', item).save(pvd);
		unlock(pvd, new ShapedRecipeBuilder(arr[1].get(), 1)::unlockedBy, arr[1].get())
				.pattern("AAA").pattern("A A").pattern("A A").define('A', item).save(pvd);
		unlock(pvd, new ShapedRecipeBuilder(arr[2].get(), 1)::unlockedBy, arr[2].get())
				.pattern("A A").pattern("AAA").pattern("AAA").define('A', item).save(pvd);
		unlock(pvd, new ShapedRecipeBuilder(arr[3].get(), 1)::unlockedBy, arr[3].get())
				.pattern("AAA").pattern("A A").define('A', item).save(pvd);
		unlock(pvd, new ShapedRecipeBuilder(arr[4].get(), 1)::unlockedBy, arr[4].get())
				.pattern("A").pattern("A").pattern("B").define('A', item).define('B', stick).save(pvd);
		unlock(pvd, new ShapedRecipeBuilder(arr[5].get(), 1)::unlockedBy, arr[5].get())
				.pattern("AA").pattern("AB").pattern(" B").define('A', item).define('B', stick).save(pvd);
		unlock(pvd, new ShapedRecipeBuilder(arr[6].get(), 1)::unlockedBy, arr[6].get())
				.pattern("A").pattern("B").pattern("B").define('A', item).define('B', stick).save(pvd);
		unlock(pvd, new ShapedRecipeBuilder(arr[7].get(), 1)::unlockedBy, arr[7].get())
				.pattern("AAA").pattern(" B ").pattern(" B ").define('A', item).define('B', stick).save(pvd);
		unlock(pvd, new ShapedRecipeBuilder(arr[8].get(), 1)::unlockedBy, arr[8].get())
				.pattern("AA").pattern(" B").pattern(" B").define('A', item).define('B', stick).save(pvd);
	}

	private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
