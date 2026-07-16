package org.turbojax.altarroles.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.turbojax.altarroles.AltarRoles;
import org.turbojax.altarroles.Role;
import org.turbojax.altarroles.WorldEvent;
import org.turbojax.altarroles.util.AltarRolesArgumentTypes;
import org.turbojax.altarroles.util.PlayerHelper;

import java.util.List;

public class AltarRolesCommand {
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final MessageComponentSerializer msgSerializer = MessageComponentSerializer.message();

    private final AltarRoles plugin;

    public AltarRolesCommand(AltarRoles plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> build(String label) {
        return Commands.literal(label)
            .then(Commands.literal("status")
                    .executes(this::status)
            )
            .then(Commands.literal("help")
                    .executes(this::help)
            )
            .then(Commands.literal("role")
                .then(Commands.literal("get")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .executes(this::getRole)
                    )
                )
                .then(Commands.literal("set")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .then(Commands.argument("role", AltarRolesArgumentTypes.ROLE)
                            .executes(this::setRole)
                        )
                    )
                )
                .then(Commands.literal("lock")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .executes(this::lockRole)
                    )
                )
            )
            .then(Commands.literal("hiddenrot")
                .then(Commands.literal("give")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .executes(this::giveHiddenRot)
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .executes(this::removeHiddenRot)
                    )
                )
                .then(Commands.literal("reveal")
                    .executes(this::revealHiddenRot)
                )
            )
            .then(Commands.literal("reload")
                .executes(this::reload)
            )
            .then(Commands.literal("eternalnight")
                .then(Commands.literal("start")
                    .executes(this::startEternalNight)
                )
                .then(Commands.literal("end")
                    .executes(this::endEternalNight)
                )
            )
            .then(Commands.literal("bloodmoon")
                .then(Commands.literal("start")
                    .executes(this::startBloodMoon)
                )
                .then(Commands.literal("end")
                    .executes(this::endBloodMoon)
                )
            )
            .then(Commands.literal("setabilitystrength")
                .then(Commands.argument("level", IntegerArgumentType.integer(1, 2))
                    .executes(this::setAbilityStrength)
                )
            )
            .then(Commands.literal("revealteam")
                .then(Commands.argument("team", AltarRolesArgumentTypes.TEAM)
                    .executes(this::revealTeam)
                )
            )
            .then(Commands.literal("hideteam")
                .then(Commands.argument("team", AltarRolesArgumentTypes.TEAM)
                    .executes(this::hideTeam)
                )
            )
            .build();
    }

    public int status(CommandContext<CommandSourceStack> ctx) {
        // TODO: View status of all settings and team reveals
        CommandSender sender = ctx.getSource().getSender();
        String fmt = """
                  <gray>Human <white>team:
                   <dark_gray>- <white>Members: <white>%d
                   <dark_gray>- <white>Revealed: %s
                  
                  <dark_red>Vampire <white>team:
                   <dark_gray>- <white>Members: <red>%d
                   <dark_gray>- <white>Revealed: %s
                  
                  <gold>Pale <white>team:
                   <dark_gray>- <white>Members: <yellow>%d
                   <dark_gray>- <white>Revealed: %s
                  
                  Active events: %s
                  """;

        // TODO: Replace with config/state lookups
        int humanCount = 0;
        int vampireCount = 0;
        int paleCount = 0;

        String humanRevealed = true ? "<green>true" : "<red>false";
        String vampireRevealed = true ? "<green>true" : "<red>false";
        String paleRevealed = false ? "<green>true" : "<red>false";

        String worldEvent = WorldEvent.NONE.asString();

        sender.sendMessage(mm.deserialize(fmt.formatted(humanCount, humanRevealed, vampireCount, vampireRevealed, paleCount, paleRevealed, worldEvent)));
        return 1;
    }

    public int help(CommandContext<CommandSourceStack> ctx) {
        // TODO: show help msg
        return 1;
    }

    public int getRole(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();

        PlayerSelectorArgumentResolver targetsResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
        List<Player> targets;
        try {
            targets = targetsResolver.resolve(ctx.getSource());
        } catch (CommandSyntaxException e) {
            sender.sendMessage(msgSerializer.deserialize(e.getRawMessage()));
            return 1;
        }

        // Printing each selected player's role
        targets.forEach(p -> {
            Role role = PlayerHelper.getRole(p);
            sender.sendMessage(p.displayName().append(Component.text(" is a ")).append(role.prettyName()));
        });

        return 1;
    }

    public int setRole(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();

        PlayerSelectorArgumentResolver targetsResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
        List<Player> targets;
        try {
            targets = targetsResolver.resolve(ctx.getSource());
        } catch (CommandSyntaxException e) {
            sender.sendMessage(msgSerializer.deserialize(e.getRawMessage()));
            return 1;
        }

        Role role = ctx.getArgument("role", Role.class);

        targets.forEach(p -> PlayerHelper.setRole(p, role));

        if (targets.size() == 1) {
            sender.sendMessage(Component.text("Set ").append(targets.getFirst().displayName()).append(Component.text("'s role to ")).append(role.prettyName()));
        } else {
            sender.sendMessage(Component.text("Set " + targets.size() + " players roles to ").append(role.prettyName()));
        }

        return 1;
    }

    public int lockRole(CommandContext<CommandSourceStack> ctx) {
        // TODO: make a player unable to be converted by anything other than the contagion signal
        return 1;
    }

    public int giveHiddenRot(CommandContext<CommandSourceStack> ctx) {
        // TODO: Parse target(s) and add/remove hidden rot from them
        return 1;
    }

    public int removeHiddenRot(CommandContext<CommandSourceStack> ctx) {
        // TODO: Parse target(s) and add/remove hidden rot from them
        return 1;
    }

    public int revealHiddenRot(CommandContext<CommandSourceStack> ctx) {
        // TODO: Converts all players with hidden rot to the TEMP_PALE team
        return 1;
    }

    public int reload(CommandContext<CommandSourceStack> ctx) {
        // TODO: Reload configs
        return 1;
    }

    public int startEternalNight(CommandContext<CommandSourceStack> ctx) {
        return 1;
    }

    public int endEternalNight(CommandContext<CommandSourceStack> ctx) {
        return 1;
    }

    public int startBloodMoon(CommandContext<CommandSourceStack> ctx) {
        return 1;
    }

    public int endBloodMoon(CommandContext<CommandSourceStack> ctx) {
        return 1;
    }

    public int setAbilityStrength(CommandContext<CommandSourceStack> ctx) {
        return 1;
    }

    public int revealTeam(CommandContext<CommandSourceStack> ctx) {
        return 1;
    }

    public int hideTeam(CommandContext<CommandSourceStack> ctx) {
        return 1;
    }
}
