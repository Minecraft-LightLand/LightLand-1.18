package dev.hikarishima.lightland.init;

import com.tterrag.registrate.Registrate;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

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
        bus.addListener(EventPriority.LOWEST, LightLand::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> LightLandClient.onCtorClient(bus, MinecraftForge.EVENT_BUS));
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
    }


}
