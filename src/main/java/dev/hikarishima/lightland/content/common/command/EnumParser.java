package dev.hikarishima.lightland.content.common.command;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.lcy0x1.util.ExceptionHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EnumParser<T extends Enum<T>> implements ArgumentType<T> {

    public static final String INVALID_ID = "lightland:argument.invalid_id";

    private static final Map<Class<?>, EnumParser<?>> CACHE = Maps.newLinkedHashMap();

    @SuppressWarnings({"unchecked", "rawtypes"})
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    public static void register() {
        ArgumentTypes.register("lightland:enum", (Class<EnumParser<?>>) (Class) EnumParser.class, new ArgumentSerializer<>() {
            @Override
            public void serializeToNetwork(EnumParser<?> e, FriendlyByteBuf packet) {
                packet.writeUtf(e.cls.getName());
            }

            @Override
            public EnumParser<?> deserializeFromNetwork(FriendlyByteBuf packet) {
                return getParser(ExceptionHandler.get(() -> (Class) Class.forName(packet.readUtf())));
            }

            @Override
            public void serializeToJson(EnumParser<?> e, JsonObject json) {
                json.addProperty("class", e.cls.getName());
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> EnumParser<T> getParser(Class<T> cls) {
        if (CACHE.containsKey(cls))
            return (EnumParser<T>) CACHE.get(cls);
        EnumParser<T> parser = new EnumParser<>(cls);
        CACHE.put(cls, parser);
        return parser;
    }

    public final Class<T> cls;

    private EnumParser(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.readUnquotedString();
        T val = ExceptionHandler.ignore(() -> Enum.valueOf(cls, str));
        if (val == null) {
            throw new DynamicCommandExceptionType((obj) -> new TranslatableComponent(INVALID_ID, obj)).createWithContext(reader, str);
        }
        return val;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> list = Arrays.stream(cls.getEnumConstants()).map(Enum::name).collect(Collectors.toList());
        return SharedSuggestionProvider.suggest(list, builder);
    }

}
