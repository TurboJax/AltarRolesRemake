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
import org.turbojax.altarroles.DataManager;
import org.turbojax.altarroles.MainConfig;
import org.turbojax.altarroles.Role;
import org.turbojax.altarroles.WorldEvent;
import org.turbojax.altarroles.util.AltarRolesArgumentTypes;
import org.turbojax.altarroles.util.PlayerHelper;

import java.util.List;

public class AltarRolesCommand {
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final MessageComponentSerializer msgSerializer = MessageComponentSerializer.message();

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
                        .executes(c -> getRole(c, c.getArgument("target", PlayerSelectorArgumentResolver.class)))
                    )
                )
                .then(Commands.literal("set")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .then(Commands.argument("role", AltarRolesArgumentTypes.ROLE)
                            .executes(c -> setRole(c, c.getArgument("target", PlayerSelectorArgumentResolver.class), c.getArgument("role", Role.class)))
                        )
                    )
                )
                .then(Commands.literal("lock")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .executes(c -> lockRole(c, c.getArgument("target", PlayerSelectorArgumentResolver.class)))
                    )
                )
                .then(Commands.literal("unlock")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .executes(c -> lockRole(c, c.getArgument("target", PlayerSelectorArgumentResolver.class)))
                    )
                )
            )
            .then(Commands.literal("hiddenrot")
                .then(Commands.literal("give")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .executes(c -> giveHiddenRot(c, c.getArgument("target", PlayerSelectorArgumentResolver.class)))
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("target", ArgumentTypes.players())
                        .executes(c -> removeHiddenRot(c, c.getArgument("target", PlayerSelectorArgumentResolver.class)))
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
                    .executes(c -> setAbilityStrength(c, c.getArgument("level", Integer.class)))
                )
            )
            .then(Commands.literal("revealteam")
                .then(Commands.argument("team", AltarRolesArgumentTypes.TEAM)
                    .executes(c -> revealTeam(c, c.getArgument("team", Role.class)))
                )
            )
            .then(Commands.literal("hideteam")
                .then(Commands.argument("team", AltarRolesArgumentTypes.TEAM)
                    .executes(c -> hideTeam(c, c.getArgument("team", Role.class)))
                )
            )
            .build();
    }

    private Component getStatusPart(Role team) {
        int memberCount = DataManager.getMemberCount(team, false);
        int onlineMemberCount = DataManager.getOnlineMemberCount(team, false);
        boolean revealed = MainConfig.isRevealed(team);

        return team.prettyName().append(mm.deserialize(" <white>team:\n" +
                        " <dark_gray>- <white>Members: <white>%d <white>(<gold>%d <white>Online)\n" +
                        " <dark_gray>- <white>Revealed: %s\n".formatted(memberCount, onlineMemberCount, revealed)));
    }

    public int status(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();

        String worldEvent = WorldEvent.NONE.asString();

        sender.sendMessage(getStatusPart(Role.TEMP_HUMAN));
        sender.sendMessage(getStatusPart(Role.TEMP_VAMPIRE));
        sender.sendMessage(getStatusPart(Role.TEMP_PALE));
        sender.sendMessage(mm.deserialize("Active events: " + worldEvent));
        return 1;
    }

    public int help(CommandContext<CommandSourceStack> ctx) {
        // TODO: show help msg
        return 1;
    }

    public int getRole(CommandContext<CommandSourceStack> ctx, PlayerSelectorArgumentResolver targetsResolver) {
        CommandSender sender = ctx.getSource().getSender();

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
            boolean locked = PlayerHelper.isLocked(p);
            sender.sendMessage(p.displayName().append(mm.deserialize("<white> is " + (locked ? "locked as a " : "a "))).append(role.prettyLongName()));
        });

        return 1;
    }

    public int setRole(CommandContext<CommandSourceStack> ctx, PlayerSelectorArgumentResolver targetsResolver, Role role) {
        CommandSender sender = ctx.getSource().getSender();

        List<Player> targets;
        try {
            targets = targetsResolver.resolve(ctx.getSource());
        } catch (CommandSyntaxException e) {
            sender.sendMessage(msgSerializer.deserialize(e.getRawMessage()));
            return 1;
        }

        // Adjusting the players roles
        targets.forEach(p -> PlayerHelper.setRole(p, role));

        // Giving feedback
        if (targets.size() == 1) {
            sender.sendMessage(Component.text("Set ").append(targets.getFirst().displayName()).append(Component.text("'s role to ")).append(role.prettyName()));
        } else {
            sender.sendMessage(Component.text("Set " + targets.size() + " players roles to ").append(role.prettyName()));
        }

        return 1;
    }

    public int lockRole(CommandContext<CommandSourceStack> ctx, PlayerSelectorArgumentResolver resolver) {
        CommandSender sender = ctx.getSource().getSender();

        List<Player> targets;
        try {
            targets = resolver.resolve(ctx.getSource());
        } catch (CommandSyntaxException e) {
            sender.sendMessage(msgSerializer.deserialize(e.getRawMessage()));
            return 1;
        }

        // Locking the targets roles
        targets.forEach(PlayerHelper::lock);

        // Giving feedback
        if (targets.size() == 1) {
            sender.sendMessage(Component.text("Locked ").append(targets.getFirst().displayName()).append(Component.text("'s role")));
        } else {
            sender.sendMessage(Component.text("Locked " + targets.size() + " players roles"));
        }

        return 1;
    }

    public int unlockRole(CommandContext<CommandSourceStack> ctx, PlayerSelectorArgumentResolver resolver) {
        CommandSender sender = ctx.getSource().getSender();

        List<Player> targets;
        try {
            targets = resolver.resolve(ctx.getSource());
        } catch (CommandSyntaxException e) {
            sender.sendMessage(msgSerializer.deserialize(e.getRawMessage()));
            return 1;
        }

        // Unlocking the targets roles
        targets.forEach(PlayerHelper::unlock);

        // Giving feedback
        if (targets.size() == 1) {
            sender.sendMessage(Component.text("Unlocked ").append(targets.getFirst().displayName()).append(Component.text("'s role")));
        } else {
            sender.sendMessage(Component.text("Unlocked " + targets.size() + " players roles"));
        }

        return 1;
    }

    public int giveHiddenRot(CommandContext<CommandSourceStack> ctx, PlayerSelectorArgumentResolver resolver) {
        CommandSender sender = ctx.getSource().getSender();

        List<Player> targets;
        try {
            targets = resolver.resolve(ctx.getSource());
        } catch (CommandSyntaxException e) {
            sender.sendMessage(msgSerializer.deserialize(e.getRawMessage()));
            return 1;
        }

        // Giving the targets hidden rot
        targets.forEach(p -> PlayerHelper.setHiddenRot(p, true));

        // Giving feedback
        if (targets.size() == 1) {
            sender.sendMessage(targets.getFirst().displayName().append(Component.text(" now has hidden rot")));
        } else {
            sender.sendMessage(Component.text("Gave " + targets.size() + " players hidden rot"));
        }

        return 1;
    }

    public int removeHiddenRot(CommandContext<CommandSourceStack> ctx, PlayerSelectorArgumentResolver resolver) {
        CommandSender sender = ctx.getSource().getSender();

        List<Player> targets;
        try {
            targets = resolver.resolve(ctx.getSource());
        } catch (CommandSyntaxException e) {
            sender.sendMessage(msgSerializer.deserialize(e.getRawMessage()));
            return 1;
        }

        // Removing the targets hidden rot
        targets.forEach(p -> PlayerHelper.setHiddenRot(p, false));

        // Giving feedback
        if (targets.size() == 1) {
            sender.sendMessage(targets.getFirst().displayName().append(Component.text(" no longer has hidden rot")));
        } else {
            sender.sendMessage(Component.text("Removed hidden rot from " + targets.size() + " players."));
        }

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

    public int setAbilityStrength(CommandContext<CommandSourceStack> ctx, int level) {
        return 1;
    }

    public int revealTeam(CommandContext<CommandSourceStack> ctx, Role team) {
        return 1;
    }

    public int hideTeam(CommandContext<CommandSourceStack> ctx, Role team) {
        return 1;
    }
}
