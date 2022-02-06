package dev.hikarishima.lightland.init;

import com.tterrag.registrate.Registrate;
import dev.hikarishima.lightland.events.ClientEntityEffectRenderEvents;
import dev.hikarishima.lightland.events.EffectSyncEvents;
import dev.hikarishima.lightland.events.ServerPlayerEvents;
import dev.hikarishima.lightland.init.registrate.*;
import dev.hikarishima.lightland.network.PacketHandler;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lightland")
public class LightLand {

    public static final String MODID = "lightland";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Registrate REGISTRATE = Registrate.create(MODID);

    public LightLand() {
        FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
        IEventBus bus = ctx.getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(LightLandClient::clientSetup);
        bus.addListener(EventPriority.LOWEST, LightLand::gatherData);
        bus.addListener(this::onParticleRegistryEvent);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> LightLandClient.onCtorClient(bus, MinecraftForge.EVENT_BUS));
        BlockRegistrate.register();
        EntityRegistrate.register();
        ItemRegistrate.register();
        MenuRegistrate.register();
        RecipeRegistrate.register();
        VanillaMagicRegistrate.register();
        ParticleRegistrate.register();
        MinecraftForge.EVENT_BUS.register(ClientEntityEffectRenderEvents.class);
        MinecraftForge.EVENT_BUS.register(EffectSyncEvents.class);
        MinecraftForge.EVENT_BUS.register(ServerPlayerEvents.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.registerPackets();
        EffectSyncEvents.init();
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
    }

    public void onParticleRegistryEvent(ParticleFactoryRegisterEvent event) {
        ParticleRegistrate.registerClient();
    }

}
