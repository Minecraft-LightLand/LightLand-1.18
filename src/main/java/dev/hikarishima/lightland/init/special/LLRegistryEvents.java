package dev.hikarishima.lightland.init.special;

import dev.lcy0x1.util.Automator;
import dev.lcy0x1.util.ExceptionHandler;
import dev.lcy0x1.util.Serializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class LLRegistryEvents {

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onNewRegistry(RegistryEvent.NewRegistry event) {
        LightLandRegistry.createRegistries();
        process(LightLandRegistry.class, IForgeRegistry.class, LLRegistryEvents::regSerializer);
    }

    private static <T extends IForgeRegistryEntry<T>> void regSerializer(IForgeRegistry<T> r) {
        new Serializer.RLClassHandler<>(r.getRegistrySuperType(), () -> r);
        new Automator.RegistryClassHandler<>(r.getRegistrySuperType(), () -> r);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> void process(Class<?> provider, Class<T> reg, Consumer<T> acceptor) {
        ExceptionHandler.run(() -> {
            for (Field f : provider.getDeclaredFields())
                if ((f.getModifiers() & Modifier.STATIC) != 0)
                    if (reg.isAssignableFrom(f.getType()))
                        ((Consumer) acceptor).accept(f.get(null));
                    else if (f.getType().isArray() && reg.isAssignableFrom(f.getType().getComponentType()))
                        for (Object o : (Object[]) f.get(null))
                            ((Consumer) acceptor).accept(o);
        });
    }

}
