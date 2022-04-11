package dev.xkmc.cuisine.init;

import com.tterrag.registrate.AbstractRegistrate;
import dev.hikarishima.lightland.init.LightLand;
import dev.xkmc.cuisine.content.veges.CornBlock;
import dev.xkmc.cuisine.init.data.WoodType;
import dev.xkmc.cuisine.init.registrate.CuisineBlocks;
import dev.xkmc.cuisine.init.registrate.CuisineItems;
import dev.xkmc.cuisine.init.registrate.CuisineWorldGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Cuisine {

	public static final String MODID = LightLand.MODID;
	public static final AbstractRegistrate<?> REGISTRATE = LightLand.REGISTRATE;

	public Cuisine() {
		CuisineBlocks.register();
		CuisineItems.register();
		MinecraftForge.EVENT_BUS.register(CornBlock.class);
		FMLJavaModLoadingContext.get().getModEventBus().register(Cuisine.class);
		FMLJavaModLoadingContext.get().getModEventBus().register(CuisineRendering.class);
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
		});
	}

}
