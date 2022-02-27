package dev.hikarishima.lightland.init.data;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.hikarishima.lightland.init.LightLand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.Locale;
import java.util.function.Function;

public class AllTags {

    public static <T> Tag.Named<T> tag(Function<ResourceLocation, Tag.Named<T>> wrapperFactory, String namespace,
                                       String path) {
        return wrapperFactory.apply(new ResourceLocation(namespace, path));
    }

    public static <T> Tag.Named<T> forgeTag(Function<ResourceLocation, Tag.Named<T>> wrapperFactory, String path) {
        return tag(wrapperFactory, "forge", path);
    }

    public static Tag.Named<Block> forgeBlockTag(String path) {
        return forgeTag(BlockTags::createOptional, path);
    }

    public static Tag.Named<Item> forgeItemTag(String path) {
        return forgeTag(ItemTags::createOptional, path);
    }

    public static Tag.Named<Fluid> forgeFluidTag(String path) {
        return forgeTag(FluidTags::createOptional, path);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOrPickaxe() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_AXE)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_AXE);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> pickaxeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, ItemBuilder<BlockItem, BlockBuilder<T, P>>> tagBlockAndItem(
            String path) {
        return b -> b.tag(forgeBlockTag(path))
                .item()
                .tag(forgeItemTag(path));
    }

    public enum NameSpace {

        MOD(LightLand.MODID, false, true),
        FORGE("forge");

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

    public enum AllBlockTags {

        ;

        public final Tag.Named<Block> tag;

        AllBlockTags() {
            this(NameSpace.MOD);
        }

        AllBlockTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllBlockTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        AllBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
            if (optional) {
                tag = BlockTags.createOptional(id);
            } else {
                tag = BlockTags.bind(id.toString());
            }
            if (alwaysDatagen) {
                LightLand.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag));
            }
        }

        public boolean matches(Block block) {
            return tag.contains(block);
        }

        public boolean matches(BlockState state) {
            return matches(state.getBlock());
        }

        public void add(Block... values) {
            LightLand.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag)
                    .add(values));
        }

        public void includeIn(Tag.Named<Block> parent) {
            LightLand.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(parent)
                    .addTag(tag));
        }

        public void includeIn(AllBlockTags parent) {
            includeIn(parent.tag);
        }

        public void includeAll(Tag.Named<Block> child) {
            LightLand.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag)
                    .addTag(child));
        }

    }

    public enum AllItemTags {
        BACKPACKS,
        DYES;

        public final Tag.Named<Item> tag;

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
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
            if (optional) {
                tag = ItemTags.createOptional(id);
            } else {
                tag = ItemTags.bind(id.toString());
            }
            if (alwaysDatagen) {
                LightLand.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag));
            }
        }

        public boolean matches(ItemStack stack) {
            return tag.contains(stack.getItem());
        }

        public void add(Item... values) {
            LightLand.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag)
                    .add(values));
        }

        public void includeIn(Tag.Named<Item> parent) {
            LightLand.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(parent)
                    .addTag(tag));
        }

        public void includeIn(AllItemTags parent) {
            includeIn(parent.tag);
        }

        public void includeAll(Tag.Named<Item> child) {
            LightLand.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag)
                    .addTag(child));
        }

    }

    public enum AllFluidTags {
        ;

        public final Tag.Named<Fluid> tag;

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
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
            if (optional) {
                tag = FluidTags.createOptional(id);
            } else {
                tag = FluidTags.bind(id.toString());
            }
            if (alwaysDatagen) {
                LightLand.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(tag));
            }
        }

        public boolean matches(Fluid fluid) {
            return fluid != null && fluid.is(tag);
        }

        public void add(Fluid... values) {
            LightLand.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(tag)
                    .add(values));
        }

        public void includeIn(Tag.Named<Fluid> parent) {
            LightLand.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(parent)
                    .addTag(tag));
        }

        public void includeIn(AllFluidTags parent) {
            includeIn(parent.tag);
        }

        public void includeAll(Tag.Named<Fluid> child) {
            LightLand.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> prov.tag(tag)
                    .addTag(child));
        }

        private static void loadClass() {
        }

    }

    public static void register() {
        AllFluidTags.loadClass();
    }

}
