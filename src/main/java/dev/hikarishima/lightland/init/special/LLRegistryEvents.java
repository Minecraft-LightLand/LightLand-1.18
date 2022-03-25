package dev.hikarishima.lightland.init.special;

import dev.hikarishima.lightland.init.registrate.RecipeRegistrate;
import dev.lcy0x1.util.Automator;
import dev.lcy0x1.util.ExceptionHandler;
import dev.lcy0x1.util.Serializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.NewRegistryEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class LLRegistryEvents {

	@SubscribeEvent
	public static void onNewRegistry(NewRegistryEvent event) {
		LightLandRegistry.createRegistries(event);
	}

	@SubscribeEvent
	public static void onFirstRegistry(RegistryEvent<Block> event){
		RecipeRegistrate.registerRecipeType();
	}

}
