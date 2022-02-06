package dev.hikarishima.lightland.init;

import com.tterrag.registrate.Registrate;
import dev.hikarishima.lightland.content.common.capability.LLPlayerData;
import dev.hikarishima.lightland.content.common.command.*;
import dev.hikarishima.lightland.events.*;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.registrate.*;
import dev.hikarishima.lightland.network.PacketHandler;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.registry.Registry;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lightland")
public class LightLand {

    public static final String MODID = "lightland";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Registrate REGISTRATE = Registrate.create(MODID);

    private static void registerRegistrates(){
        BlockRegistrate.register();
        EntityRegistrate.register();
        ItemRegistrate.register();
        MenuRegistrate.register();
        RecipeRegistrate.register();
        VanillaMagicRegistrate.register();
        ParticleRegistrate.register();
    }

    private static void registerForgeEvents(){
        MinecraftForge.EVENT_BUS.register(ClientEntityEffectRenderEvents.class);
        MinecraftForge.EVENT_BUS.register(EffectSyncEvents.class);
        MinecraftForge.EVENT_BUS.register(ItemUseEventHandler.class);
        MinecraftForge.EVENT_BUS.register(CapabilityEvents.class);
        MinecraftForge.EVENT_BUS.register(GenericEventHandler.class);
        MinecraftForge.EVENT_BUS.register(ArcaneDamageEventHandler.class);
    }

    private static void registerCommands(){
        EnumParser.register();
        RegistryParser.register();
        BaseCommand.LIST.add(MainCommand::new);
        BaseCommand.LIST.add(ArcaneCommand::new);
    }

    public LightLand() {
        FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
        IEventBus bus = ctx.getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(LightLandClient::clientSetup);
        bus.addListener(EventPriority.LOWEST, LightLand::gatherData);
        bus.addListener(this::onParticleRegistryEvent);
        bus.addListener(this::registerCaps);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> LightLandClient.onCtorClient(bus, MinecraftForge.EVENT_BUS));
        registerRegistrates();
        registerForgeEvents();
        registerCommands();
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.registerPackets();
        EffectSyncEvents.init();
    }

    public static void gatherData(GatherDataEvent event) {
        LangData.addTranslations(REGISTRATE::addRawLang);
    }

    public void onParticleRegistryEvent(ParticleFactoryRegisterEvent event) {
        ParticleRegistrate.registerClient();
    }

    public void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(LLPlayerData.class);
    }

}
