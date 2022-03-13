package dev.hikarishima.lightland.content.magic.products;

import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.magic.products.info.TypeConfig;
import dev.hikarishima.lightland.content.magic.products.recipe.IMagicRecipe;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import dev.hikarishima.lightland.network.config.ProductTypeConfig;
import dev.lcy0x1.base.NamedEntry;
import dev.lcy0x1.util.NBTObj;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Function;
import java.util.function.Supplier;

public class MagicProductType<I extends IForgeRegistryEntry<I>, P extends MagicProduct<I, P>> extends NamedEntry<MagicProductType<?, ?>> {

	public final Class<P> cls;
	public final MagicFactory<I, P> fac;
	public final Function<ResourceLocation, I> getter;
	public final Function<I, String> namer;
	public final Supplier<IForgeRegistry<I>> registry;
	public final MagicElement elem;

	public MagicProductType(Class<P> cls, MagicFactory<I, P> fac, Supplier<IForgeRegistry<I>> registry,
							Function<I, String> namer, MagicElement elem) {
		super(() -> LightLandRegistry.PRODUCT_TYPE);
		this.cls = cls;
		this.fac = fac;
		this.getter = (s) -> registry.get().getValue(s);
		this.namer = namer;
		this.registry = registry;
		this.elem = elem;
	}

	public TypeConfig getDisplay() {
		return ProductTypeConfig.getConfig(getRegistryName());
	}

	@FunctionalInterface
	public interface MagicFactory<I extends IForgeRegistryEntry<I>, P extends MagicProduct<I, P>> {

		P get(LLPlayerData player, NBTObj nbtManager, ResourceLocation rl, IMagicRecipe<?> r);

	}

}
