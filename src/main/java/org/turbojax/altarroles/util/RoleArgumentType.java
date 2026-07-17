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
import java.util.stream.Stream;

import org.turbojax.altarroles.Role;

public class RoleArgumentType implements CustomArgumentType<Role,String> {
    private static final List<String> EXAMPLES = List.of("temp_human", "temp_vampire", "true_pale");
    private static final DynamicCommandExceptionType PARSE_ERROR = new DynamicCommandExceptionType(team -> new LiteralMessage("Invalid team \"" + team + "\""));

    @Override
    public Role parse(StringReader reader) throws CommandSyntaxException {
        String role = reader.readUnquotedString();

        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException err) {
            throw PARSE_ERROR.create(role);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Stream.of(Role.values())
            .map(Enum::name)
            .map(String::toLowerCase)
            .filter(s -> s.contains(builder.getRemaining().toLowerCase()))
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