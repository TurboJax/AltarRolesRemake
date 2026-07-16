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
import org.turbojax.altarroles.Role;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TeamArgumentType implements CustomArgumentType<Role,String> {
    private static final List<String> EXAMPLES = List.of("human", "vampire", "pale");
    private static final DynamicCommandExceptionType PARSE_ERROR = new DynamicCommandExceptionType(team -> new LiteralMessage("Invalid team \"" + team + "\""));

    @Override
    public Role parse(StringReader reader) throws CommandSyntaxException {
        String team = reader.readUnquotedString();

        if (team.equalsIgnoreCase("human")) return Role.TEMP_HUMAN;
        if (team.equalsIgnoreCase("vampire")) return Role.TEMP_VAMPIRE;
        if (team.equalsIgnoreCase("pale")) return Role.TEMP_PALE;

        throw PARSE_ERROR.create(team);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        builder.suggest("human");
        builder.suggest("vampire");
        builder.suggest("pale");

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