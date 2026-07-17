package org.turbojax.altarroles;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.Map;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbojax.altarroles.commands.AltarRolesCommand;
import org.turbojax.altarroles.listeners.PlayerDeathListener;
import org.turbojax.altarroles.listeners.PlayerJoinListener;

public final class AltarRoles extends JavaPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger("AltarRoles");

    @Override
    public void onEnable() {
        // Saving the default config
        MainConfig.load();

        // Registering commands
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new AltarRolesCommand().build("altarroles"));
        });

        // Registering event handlers
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        // Registering the teams
        Stream.of(Role.values()).forEach(AltarRoles::getTeam);
    }

    public static Team getTeam(Role role) {
        String key = role.name().toLowerCase();
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(key);
        if (team == null) {
            return Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(key);
        }

        return team;
    }

    public static void setTeamColor(Team team, Role role) {
        team.prefix(Component.text("", MainConfig.getColor(role)));
    }

    public static void updateColors() {
        Stream.of(Role.values())
            .map(r -> Map.entry(r, getTeam(r)))
            .forEach(e -> setTeamColor(e.getValue(), e.getKey()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DataManager.save();
        MainConfig.save();
    }
}
