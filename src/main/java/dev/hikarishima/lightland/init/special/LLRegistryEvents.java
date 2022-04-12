package dev.hikarishima.lightland.init.special;

import dev.hikarishima.lightland.init.registrate.LightlandRecipe;
import dev.xkmc.cuisine.init.registrate.CuisineRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class LLRegistryEvents {

	@SubscribeEvent
	public static void onNewRegistry(NewRegistryEvent event) {
		LightLandRegistry.createRegistries(event);
	}

	@SubscribeEvent
	public static void onFirstRegistry(RegistryEvent<Block> event){
		LightlandRecipe.registerRecipeType();
		CuisineRecipe.registerRecipeType();
	}

}
