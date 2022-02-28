package dev.hikarishima.lightland.content.common.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.hikarishima.lightland.content.arcane.internal.Arcane;
import dev.hikarishima.lightland.content.arcane.internal.ArcaneType;
import dev.hikarishima.lightland.content.magic.products.MagicElement;
import dev.hikarishima.lightland.content.magic.spell.internal.Spell;
import dev.hikarishima.lightland.content.profession.Profession;
import dev.hikarishima.lightland.content.skill.internal.Skill;
import dev.hikarishima.lightland.init.data.LangData;
import dev.hikarishima.lightland.init.special.LightLandRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes"})
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RegistryParser<T extends IForgeRegistryEntry<T>> implements ArgumentType<T> {

    public static final Set<RegistryParser<?>> SET = new HashSet<>();
    public static final RegistryParser<MagicElement> ELEMENT = new RegistryParser<>(MagicElement.class, () -> LightLandRegistry.ELEMENT);
    public static final RegistryParser<ArcaneType> ARCANE_TYPE = new RegistryParser<>(ArcaneType.class, () -> LightLandRegistry.ARCANE_TYPE);
    public static final RegistryParser<Arcane> ARCANE = new RegistryParser<>(Arcane.class, () -> LightLandRegistry.ARCANE);
    public static final RegistryParser<Profession> PROFESSION = new RegistryParser<>(Profession.class, () -> LightLandRegistry.PROFESSION);
    public static final RegistryParser<Spell<?, ?>> SPELL = new RegistryParser(Spell.class, () -> LightLandRegistry.SPELL);
    public static final RegistryParser<Skill<?, ?>> SKILL = new RegistryParser(Skill.class, () -> LightLandRegistry.SKILL);

    public static void register() {
        ArgumentTypes.register("lightland:registry", (Class<RegistryParser<?>>) (Class) RegistryParser.class, new ArgumentSerializer<>() {
            @Override
            public void serializeToNetwork(RegistryParser<?> e, FriendlyByteBuf packet) {
                IForgeRegistry<?> reg = e.registry.get();
                packet.writeUtf(reg.getRegistryName().toString());
            }

            @Override
            public RegistryParser<?> deserializeFromNetwork(FriendlyByteBuf packet) {
                String name = packet.readUtf();
                return Objects.requireNonNull(SET.stream().filter(e -> e.registry.get().getRegistryName().toString().equals(name)).findFirst().orElse(null));
            }

            @Override
            public void serializeToJson(RegistryParser<?> e, JsonObject json) {
                IForgeRegistry<?> reg = e.registry.get();
                json.addProperty("id", reg.getRegistryName().toString());
            }
        });
    }

    public final Class<T> cls;
    public final Supplier<IForgeRegistry<T>> registry;

    public RegistryParser(Class<T> cls, Supplier<IForgeRegistry<T>> registry) {
        this.cls = cls;
        this.registry = registry;
        SET.add(this);
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        ResourceLocation rl = ResourceLocation.read(reader);
        T val = registry.get().getValue(rl);
        if (val == null) {
            reader.setCursor(cursor);
            throw new DynamicCommandExceptionType(LangData.IDS.INVALID_ID::get).createWithContext(reader, rl.toString());
        }
        return val;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(registry.get().getKeys(), builder);
    }

}
