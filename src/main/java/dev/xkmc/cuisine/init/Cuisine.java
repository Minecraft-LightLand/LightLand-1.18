package dev.xkmc.cuisine.init;

import dev.hikarishima.lightland.init.LightLand;
import dev.xkmc.l2library.base.LcyRegistrate;
import dev.xkmc.cuisine.content.misc.CuisineBottleItem;
import dev.xkmc.cuisine.content.tools.base.tile.TileInfoOverlay;
import dev.xkmc.cuisine.content.veges.CornBlock;
import dev.xkmc.cuisine.init.data.CuisineTags;
import dev.xkmc.cuisine.init.data.LangData;
import dev.xkmc.cuisine.init.data.RecipeGen;
import dev.xkmc.cuisine.init.data.WoodType;
import dev.xkmc.cuisine.init.registrate.*;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class Cuisine {

	public static final String MODID = LightLand.MODID;
	public static final LcyRegistrate REGISTRATE = LightLand.REGISTRATE;

	public Cuisine() {
		CuisineBlocks.register();
		CuisineItems.register();
		CuisineFluids.register();
		CuisineTags.register();
		CuisineRecipes.register(FMLJavaModLoadingContext.get().getModEventBus());
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
			OverlayRegistry.registerOverlayAbove(ForgeIngameGui.CROSSHAIR_ELEMENT, "Block Info", new TileInfoOverlay());
		});
	}

}
