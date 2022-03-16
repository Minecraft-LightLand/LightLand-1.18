package dev.hikarishima.lightland.init;

import dev.hikarishima.lightland.compat.GeneralCompatHandler;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.common.command.*;
import dev.hikarishima.lightland.events.DamageEventHandler;
import dev.hikarishima.lightland.events.ItemUseEventHandler;
import dev.hikarishima.lightland.events.MiscEventHandler;
import dev.hikarishima.lightland.events.generic.CapabilityEvents;
import dev.hikarishima.lightland.events.generic.ClientEntityEffectRenderEvents;
import dev.hikarishima.lightland.events.generic.EffectSyncEvents;
import dev.hikarishima.lightland.events.generic.GenericEventHandler;
import dev.hikarishima.lightland.init.data.AllTags;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.registrate.*;
import dev.hikarishima.lightland.init.special.LLRegistrate;
import dev.hikarishima.lightland.network.PacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
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
	public static final LLRegistrate REGISTRATE = new LLRegistrate(MODID);

	private static void registerRegistrates() {
		BlockRegistrate.register();
		EntityRegistrate.register();
		ItemRegistrate.register();
		MenuRegistrate.register();
		RecipeRegistrate.register();
		VanillaMagicRegistrate.register();
		ParticleRegistrate.register();
		AllTags.register();
		GeneralCompatHandler.handle(GeneralCompatHandler.Stage.INIT);
	}

	private static void registerForgeEvents() {
		MinecraftForge.EVENT_BUS.register(ClientEntityEffectRenderEvents.class);
		MinecraftForge.EVENT_BUS.register(EffectSyncEvents.class);
		MinecraftForge.EVENT_BUS.register(ItemUseEventHandler.class);
		MinecraftForge.EVENT_BUS.register(CapabilityEvents.class);
		MinecraftForge.EVENT_BUS.register(GenericEventHandler.class);
		MinecraftForge.EVENT_BUS.register(DamageEventHandler.class);
		MinecraftForge.EVENT_BUS.register(MiscEventHandler.class);
	}

	private static void registerModBusEvents(IEventBus bus) {
		bus.addListener(LightLand::setup);
		bus.addListener(LightLandClient::clientSetup);
		bus.addListener(EventPriority.LOWEST, LightLand::gatherData);
		bus.addListener(LightLand::onParticleRegistryEvent);
		bus.addListener(LightLand::registerCaps);
		bus.addListener(EntityRegistrate::registerEntityAttributes);
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
		registerRegistrates();
		registerForgeEvents();
		registerCommands();
	}

	private static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PacketHandler.registerPackets();
			EffectSyncEvents.init();
			VanillaMagicRegistrate.registerBrewingRecipe();
		});
	}

	public static void gatherData(GatherDataEvent event) {
		LangData.addTranslations(REGISTRATE::addRawLang);
	}

	public static void onParticleRegistryEvent(ParticleFactoryRegisterEvent event) {
		ParticleRegistrate.registerClient();
	}

	public static void registerCaps(RegisterCapabilitiesEvent event) {
		event.register(LLPlayerData.class);
	}

}
