package dev.xkmc.cuisine.init.data;

import com.google.common.collect.Lists;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.cuisine.init.Cuisine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Arrays;
import java.util.Collections;

import static dev.xkmc.cuisine.init.Cuisine.REGISTRATE;

public class CuisineTags {

	public static <T extends IForgeRegistryEntry<T>> TagKey<T> optionalTag(IForgeRegistry<T> registry, ResourceLocation id) {
		return registry.tags().createOptionalTagKey(id, Collections.emptySet());
	}

	public static <T extends IForgeRegistryEntry<T>> TagKey<T> forgeTag(IForgeRegistry<T> registry, String path) {
		return optionalTag(registry, new ResourceLocation("forge", path));
	}

	public enum NameSpace {

		MOD(Cuisine.MODID, false, true),
		FORGE("forge", false, true),
		TIC("tconstruct");

		public final String id;
		public final boolean optionalDefault;
		public final boolean alwaysDatagenDefault;

		NameSpace(String id) {
			this(id, true, false);
		}

		NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
			this.id = id;
			this.optionalDefault = optionalDefault;
			this.alwaysDatagenDefault = alwaysDatagenDefault;
		}

	}

	public enum AllItemTags {
		CUBED, DICED, SLICED, SHREDDED, MINCED, PASTE,
		CHICKEN, BEEF, PORK, MUTTON, RABBIT, COD, SALMON,
		RED_MEAT, WHITE_MEAT, FISH,
		STAPLE, MEAT, VEGES, SIDE, FRUIT, CONDIMENT,
		SEAFOOD, ABSORB_SALT,
		GREASY, SALTY, SWEET, SPICY, NUMB, SOUR, KELP, SESAME,
		CAN_COOK;

		public final TagKey<Item> tag;

		AllItemTags() {
			this(NameSpace.MOD);
		}

		AllItemTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? LangData.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.ITEMS, id);
			} else {
				tag = ItemTags.create(id);
			}
			if (alwaysDatagen) {
				REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag));
			}
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Item item) {
			return item.builtInRegistryHolder().is(tag);
		}

		public boolean matches(ItemStack stack) {
			return stack.is(tag);
		}

		public void add(Item... values) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag)
					.add(values));
		}

		public void includeIn(TagKey<Item> parent) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(parent)
					.addTag(tag));
		}

		public void includeIn(AllItemTags parent) {
			includeIn(parent.tag);
		}

		public void includeAll(TagKey<Item> child) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag)
					.addTag(child));
		}

		public void includeAll(AllItemTags... children) {
			for (AllItemTags child : children)
				includeAll(child.tag);
		}

	}

	public enum AllFluidTags {
		MILK(NameSpace.FORGE),
		CHOCOLATE(NameSpace.FORGE),
		HONEY(NameSpace.FORGE),
		TEA(NameSpace.FORGE),
		GREASY, SALTY, SWEET, SPICY, SOUR, KELP, SESAME, VEGES,
		JAR_ACCEPT;

		public final TagKey<Fluid> tag;

		AllFluidTags() {
			this(NameSpace.MOD);
		}

		AllFluidTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? LangData.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.FLUIDS, id);
			} else {
				tag = FluidTags.create(id);
			}
			if (alwaysDatagen) {
				REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(tag));
			}
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Fluid fluid) {
			return fluid.is(tag);
		}

		public boolean matches(FluidState state) {
			return state.is(tag);
		}

		public void add(Fluid... values) {
			REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(tag)
					.add(values));
		}

		public void includeIn(TagKey<Fluid> parent) {
			REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(parent)
					.addTag(tag));
		}

		public void includeIn(com.simibubi.create.AllTags.AllFluidTags parent) {
			includeIn(parent.tag);
		}

		public void includeAll(TagKey<Fluid> child) {
			REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(tag)
					.addTag(child));
		}

		static void register() {
			JAR_ACCEPT.add(Fluids.WATER);
			JAR_ACCEPT.includeAll(MILK.tag);
			JAR_ACCEPT.includeAll(CHOCOLATE.tag);
			JAR_ACCEPT.includeAll(TEA.tag);
			JAR_ACCEPT.includeAll(HONEY.tag);
		}

	}

	public static void register() {
		AllFluidTags.register();
		// vanilla
		AllItemTags.STAPLE.add(Items.POTATO, Items.BEETROOT);
		AllItemTags.MEAT.add(Items.CHICKEN, Items.PORKCHOP, Items.BEEF, Items.MUTTON,
				Items.RABBIT, Items.RABBIT_FOOT, Items.EGG, Items.ROTTEN_FLESH,
				Items.SALMON, Items.COD, Items.TROPICAL_FISH, Items.PUFFERFISH);
		AllItemTags.VEGES.add(Items.CARROT, Items.KELP, Items.DRIED_KELP, Items.SEAGRASS,
				Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, Items.PUMPKIN, Items.PUMPKIN_SEEDS, Items.BAMBOO);
		AllItemTags.FRUIT.add(Items.APPLE, Items.MELON_SLICE, Items.SUGAR_CANE,
				Items.SWEET_BERRIES, Items.GLOW_BERRIES, Items.CHORUS_FRUIT);
		AllItemTags.SIDE.add(Items.NETHER_WART, Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS,
				Items.SPIDER_EYE, Items.BONE, Items.HONEYCOMB, Items.COCOA_BEANS);
		AllItemTags.CONDIMENT.add(Items.SUGAR);

		AllItemTags.ABSORB_SALT.add(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS);
		AllItemTags.GREASY.add(Items.PORKCHOP, Items.BEEF, Items.MUTTON, Items.SALMON, Items.COD);
		AllItemTags.SWEET.add(Items.APPLE, Items.MELON_SLICE, Items.SUGAR_CANE,
				Items.SWEET_BERRIES, Items.GLOW_BERRIES, Items.SUGAR, Items.HONEYCOMB, Items.COCOA_BEANS);
		AllItemTags.KELP.add(Items.KELP, Items.DRIED_KELP);

		AllItemTags.CAN_COOK.includeAll(AllItemTags.STAPLE, AllItemTags.MEAT, AllItemTags.VEGES, AllItemTags.FRUIT,
				AllItemTags.SIDE, AllItemTags.CONDIMENT);
	}

	@SuppressWarnings({"unsafe", "unchecked"})
	public static TagKey<Item>[] map(AllItemTags... tags) {
		return (TagKey<Item>[]) Arrays.stream(tags).map(e -> e.tag).toArray(TagKey[]::new);
	}


	@SuppressWarnings({"unsafe", "unchecked"})
	public static TagKey<Item>[] map(AllItemTags tag, AllItemTags... tags) {
		return (TagKey<Item>[]) Lists.asList(tag, tags).stream().map(e -> e.tag).toArray(TagKey[]::new);
	}

}
