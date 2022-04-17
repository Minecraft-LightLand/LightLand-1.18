package dev.xkmc.cuisine.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.hikarishima.lightland.init.LightLand;
import dev.hikarishima.lightland.init.special.LLRegistrate;
import dev.xkmc.cuisine.content.misc.CuisineBottleItem;
import dev.xkmc.cuisine.content.veges.CornBlock;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.data.LangData;
import dev.xkmc.cuisine.init.data.RecipeGen;
import dev.xkmc.cuisine.init.data.WoodType;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineFluids;
import dev.xkmc.cuisine.init.registrate.CuisineItems;
import dev.xkmc.cuisine.init.registrate.CuisineWorldGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class Cuisine {

	public static final String MODID = LightLand.MODID;
	public static final LLRegistrate REGISTRATE = LightLand.REGISTRATE;

	public Cuisine() {
		CuisineBlocks.register();
		CuisineItems.register();
		CuisineFluids.register();
		CuisineTags.register();
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);

		MinecraftForge.EVENT_BUS.register(CornBlock.class);
		MinecraftForge.EVENT_BUS.register(CuisineBottleItem.class);
		FMLJavaModLoadingContext.get().getModEventBus().register(Cuisine.class);
		FMLJavaModLoadingContext.get().getModEventBus().register(CuisineRendering.class);
	}

	@SubscribeEvent
	public static void onDataGen(GatherDataEvent event) {
	}

	@SubscribeEvent
	public static void onCommonInit(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			WoodType.onCommonInit();
			CuisineWorldGen.init();
		});
	}

	@SubscribeEvent
	public static void onClientInit(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			WoodType.onClientInit();
			CuisineBottleItem.onClientInit();
		});
	}

}
