package dev.hikarishima.lightland.init.special;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.lcy0x1.base.NamedEntry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

public class LLRegistrate extends AbstractRegistrate<LLRegistrate> {
    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    public LLRegistrate(String modid) {
        super(modid);
        registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public <T extends NamedEntry<T>, P extends T> GenericBuilder<T, P> generic(Class<T> cls, String id, NonNullSupplier<P> sup) {
        return entry(id, cb -> new GenericBuilder<>(this, id, cb, cls, sup));
    }

    public static class GenericBuilder<T extends NamedEntry<T>, P extends T> extends AbstractBuilder<T, P, LLRegistrate, GenericBuilder<T, P>> {

        private final NonNullSupplier<P> sup;

        GenericBuilder(LLRegistrate parent, String name, BuilderCallback callback, Class<T> registryType, NonNullSupplier<P> sup) {
            super(parent, parent, name, callback, registryType);
            this.sup = sup;
        }

        @Override
        protected @NonnullType @NotNull P createEntry() {
            return sup.get();
        }

        public GenericBuilder<T, P> defaultLang() {
            return lang(NamedEntry::getDescriptionId);
        }

    }

}
