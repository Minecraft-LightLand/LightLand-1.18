package dev.hikarishima.lightland.init.data;

import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.repack.registrate.util.DataIngredient;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.hikarishima.lightland.init.registrate.LightlandBlocks;
import dev.hikarishima.lightland.init.registrate.LightlandItems;
import dev.hikarishima.lightland.init.registrate.LightlandRecipe;
import dev.xkmc.l2library.recipe.CustomShapelessBuilder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

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
		{
			for (int i = 0; i < 16; i++) {
				DyeColor color = DyeColor.values()[i];
				Item wool = ForgeRegistries.ITEMS.getValue(new ResourceLocation(color.getName() + "_wool"));
				Item dye = ForgeRegistries.ITEMS.getValue(new ResourceLocation(color.getName() + "_dye"));
				Item backpack = LightlandItems.BACKPACKS[i].get();

				unlock(pvd, new ShapedRecipeBuilder(backpack, 1)::unlockedBy, backpack)
						.group("backpack_craft").pattern("ADA").pattern("BCB").pattern("ADA")
						.define('A', Tags.Items.LEATHER).define('B', wool)
						.define('C', Items.CHEST).define('D', Items.IRON_INGOT)
						.save(pvd, "lightland:shaped/craft_backpack_" + color.getName());

				unlock(pvd, new CustomShapelessBuilder<>(LightlandRecipe.RSC_BAG_DYE, backpack, 1)::unlockedBy, backpack)
						.group("backpack_dye").requires(Ingredient.of(AllTags.AllItemTags.BACKPACKS.tag))
						.requires(Ingredient.of(dye)).save(pvd, "lightland:shapeless/dye_backpack_" + color.getName());

				unlock(pvd, new UpgradeRecipeBuilder(LightlandRecipe.RSC_BAG_UPGRADE.get(), Ingredient.of(backpack),
						Ingredient.of(LightlandItems.ENDER_POCKET.get()), backpack)::unlocks, backpack)
						.save(pvd, "lightland:smithing/upgrade_backpack_" + color.getName());

				Item storage = LightlandItems.DIMENSIONAL_STORAGE[i].get();

				unlock(pvd, new ShapedRecipeBuilder(storage, 1)::unlockedBy, storage)
						.group("dimensional_storage_craft").pattern("DAD").pattern("ACA").pattern("BAB")
						.define('A', LightlandItems.ENDER_POCKET.get()).define('B', wool)
						.define('C', Items.ENDER_CHEST).define('D', Items.POPPED_CHORUS_FRUIT)
						.save(pvd, "lightland:shaped/craft_storage_" + color.getName());
			}
			Item ender = LightlandItems.ENDER_BACKPACK.get();
			unlock(pvd, new ShapedRecipeBuilder(ender, 1)::unlockedBy, ender)
					.pattern("ADA").pattern("BCB").pattern("ADA")
					.define('A', Tags.Items.LEATHER).define('B', Items.ENDER_PEARL)
					.define('C', Items.ENDER_CHEST).define('D', Items.IRON_INGOT)
					.save(pvd);
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
