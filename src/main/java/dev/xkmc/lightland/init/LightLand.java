package dev.xkmc.lightland.init;

import dev.xkmc.l2library.base.LcyRegistrate;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.l2library.serial.handler.Handlers;
import dev.xkmc.lightland.compat.GeneralCompatHandler;
import dev.xkmc.lightland.content.common.capability.player.LLPlayerData;
import dev.xkmc.lightland.content.common.command.*;
import dev.xkmc.lightland.events.DamageEventHandler;
import dev.xkmc.lightland.events.ItemUseEventHandler;
import dev.xkmc.lightland.events.MiscEventHandler;
import dev.xkmc.lightland.events.WorldGenEventHandler;
import dev.xkmc.lightland.events.generic.CapabilityEvents;
import dev.xkmc.lightland.events.generic.ClientEntityEffectRenderEvents;
import dev.xkmc.l2library.effects.EffectSyncEvents;
import dev.xkmc.lightland.events.generic.GenericEventHandler;
import dev.xkmc.lightland.init.data.AllTags;
import dev.xkmc.lightland.init.data.LangData;
import dev.xkmc.lightland.init.data.RecipeGen;
import dev.xkmc.lightland.init.registrate.*;
import dev.xkmc.lightland.init.worldgenreg.StructureRegistrate;
import dev.xkmc.lightland.init.worldgenreg.WorldGenRegistrate;
import dev.xkmc.lightland.network.NetworkManager;
import dev.xkmc.lightland.util.EffectAddUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lightland")
public class LightLand {

	public static final String MODID = "lightland";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final LcyRegistrate REGISTRATE = new LcyRegistrate(MODID);

	private static void registerRegistrates(IEventBus bus) {
		ForgeMod.enableMilkFluid();
		LightlandBlocks.register();
		LightlandEntities.register();
		LightlandItems.register();
		LightlandMenu.register();
		LightlandRecipe.register(bus);
		LightlandVanillaMagic.register();
		LightlandParticle.register();
		WorldGenRegistrate.register();
		StructureRegistrate.register();
		AllTags.register();
		Handlers.register();
		NetworkManager.register();
		GeneralCompatHandler.handle(GeneralCompatHandler.Stage.INIT);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
	}

	private static void registerForgeEvents() {
		MinecraftForge.EVENT_BUS.register(ClientEntityEffectRenderEvents.class);
		MinecraftForge.EVENT_BUS.register(EffectSyncEvents.class);
		MinecraftForge.EVENT_BUS.register(ItemUseEventHandler.class);
		MinecraftForge.EVENT_BUS.register(CapabilityEvents.class);
		MinecraftForge.EVENT_BUS.register(GenericEventHandler.class);
		MinecraftForge.EVENT_BUS.register(DamageEventHandler.class);
		MinecraftForge.EVENT_BUS.register(MiscEventHandler.class);
		MinecraftForge.EVENT_BUS.register(WorldGenEventHandler.class);

	}

	private static void registerModBusEvents(IEventBus bus) {
		bus.addListener(LightLand::setup);
		bus.addListener(LightLandClient::clientSetup);
		bus.addListener(EventPriority.LOWEST, LightLand::gatherData);
		bus.addListener(LightLand::onParticleRegistryEvent);
		bus.addListener(LightLand::registerCaps);
		bus.addListener(LightlandEntities::registerEntityAttributes);
	}

	private static void registerCommands() {
		EnumParser.register();
		RegistryParser.register();
		BaseCommand.LIST.add(MainCommand::new);
		BaseCommand.LIST.add(ArcaneCommand::new);
		BaseCommand.LIST.add(MagicCommand::new);
	}

	public LightLand() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		registerModBusEvents(bus);
		GeckoLib.initialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> LightLandClient.onCtorClient(bus, MinecraftForge.EVENT_BUS));
		registerRegistrates(bus);
		registerForgeEvents();
		registerCommands();
	}

	private static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			EffectAddUtil.init();
			LightlandVanillaMagic.registerBrewingRecipe();
		});
		StructureRegistrate.commonSetup(event);
	}

	public static void gatherData(GatherDataEvent event) {
		LangData.addTranslations(REGISTRATE::addRawLang);
	}

	public static void onParticleRegistryEvent(ParticleFactoryRegisterEvent event) {
		LightlandParticle.registerClient();
	}

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.register(LLPlayerData.class);
	}

}
