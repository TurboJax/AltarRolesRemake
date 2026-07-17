package org.turbojax.altarroles.util;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.turbojax.altarroles.WorldEvent;

public class WorldEventArgumentType implements CustomArgumentType<WorldEvent,String> {
    private static final List<String> EXAMPLES = List.of("blood_moon", "eternal_night");
    private static final DynamicCommandExceptionType PARSE_ERROR = new DynamicCommandExceptionType(team -> new LiteralMessage("Invalid team \"" + team + "\""));

    @Override
    public WorldEvent parse(StringReader reader) throws CommandSyntaxException {
        String key = reader.readUnquotedString();

        if (key.equalsIgnoreCase("blood_moon")) return WorldEvent.BLOOD_MOON;
        if (key.equalsIgnoreCase("eternal_night")) return WorldEvent.ETERNAL_NIGHT;

        throw PARSE_ERROR.create(key);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        EXAMPLES.stream()
            .filter(s -> s.contains(builder.getRemainingLowerCase()))
            .sorted()
            .forEach(builder::suggest);

        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}