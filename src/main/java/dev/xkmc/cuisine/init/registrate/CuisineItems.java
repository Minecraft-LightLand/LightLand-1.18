package dev.xkmc.cuisine.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.cuisine.content.misc.CuisineBottleItem;
import dev.xkmc.cuisine.init.Cuisine;
import dev.xkmc.cuisine.init.data.CuisineCropType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public class CuisineItems {

	public static class Tab extends CreativeModeTab {

		private final Supplier<Supplier<Item>> icon;

		public Tab(String id, Supplier<Supplier<Item>> icon) {
			super(Cuisine.MODID + "." + id);
			this.icon = icon;
		}

		@Override
		public ItemStack makeIcon() {
			return icon.get().get().getDefaultInstance();
		}
	}

	public static final Tab TAB_MAIN = new Tab("cuisine", () -> CuisineCropType.CHILI::getSeed);

	public static final ItemEntry<CuisineBottleItem> BOTTLE;

	static {
		SimpleItem.register();
		BOTTLE = REGISTRATE.item("liquid_bottle", p ->
						new CuisineBottleItem(p.stacksTo(1)))
				.model((ctx, pvd) -> {
					ModelFile[] overrides = new ModelFile[5];
					for (int i = 0; i < 5; i++) {
						ItemModelBuilder builder = pvd.withExistingParent(ctx.getName() + "_" + (i + 1), "minecraft:generated");
						builder.texture("layer0", "item/liquid_bottle_" + (i + 1));
						builder.texture("layer1", "item/liquid_bottle_base");
						overrides[i] = builder;
					}
					ItemModelBuilder builder = pvd.withExistingParent(ctx.getName(), "minecraft:generated");
					builder.texture("layer0", "item/liquid_bottle_0");
					builder.texture("layer1", "item/liquid_bottle_base");
					builder.override().predicate(new ResourceLocation(Cuisine.MODID, "amount"), 0.1f).model(overrides[0]).end()
							.override().predicate(new ResourceLocation(Cuisine.MODID, "amount"), 0.3f).model(overrides[1]).end()
							.override().predicate(new ResourceLocation(Cuisine.MODID, "amount"), 0.5f).model(overrides[2]).end()
							.override().predicate(new ResourceLocation(Cuisine.MODID, "amount"), 0.7f).model(overrides[3]).end()
							.override().predicate(new ResourceLocation(Cuisine.MODID, "amount"), 0.9f).model(overrides[4]).end();
				}).color(() -> () -> (stack, index) -> index == 0 ? CuisineBottleItem.getFluidColor(stack) : 0xFFFFFF)
				.defaultLang().register();
		PlateItem.register();
		ProcessedMeat.Meat.register();
	}

	public static void register() {

	}

}
