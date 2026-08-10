package org.turbojax.altarroles;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbojax.altarroles.commands.AltarRolesCommand;
import org.turbojax.altarroles.listeners.PlayerDeathListener;
import org.turbojax.altarroles.listeners.PlayerJoinListener;
import org.turbojax.altarroles.listeners.PlayerMoveListener;

public final class AltarRoles extends JavaPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger("AltarRoles");

    @Override
    public void onEnable() {
        // Saving the default config
        MainConfig.load();
        DataManager.load();

        // Registering commands
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new AltarRolesCommand().build("altarroles"));
        });

        // Registering event handlers
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);

        // Registering the teams
        Stream.of(Role.values()).forEach(AltarRoles::getTeam);
    }

    public static Team getTeam(Role role) {
        String key = role.name().toLowerCase();

        // Getting an existing team
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(key);

        // Creating the team if it doesn't exist
        if (team == null) {
            team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(key);
        }

        // Setting the team's color if they're revealed.
        // If the team isn't revealed, their color is white
        if (MainConfig.isRevealed(role)) {
            team.color(MainConfig.getColor(role));
        } else {
            team.color(NamedTextColor.WHITE);
        }

        return team;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DataManager.save();
        MainConfig.save();
    }
}
