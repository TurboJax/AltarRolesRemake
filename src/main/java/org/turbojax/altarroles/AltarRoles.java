package org.turbojax.altarroles;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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
        MainConfig.save();
        MainConfig.load();

        // Registering commands
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new AltarRolesCommand().build("altarroles"));
        });

        // Registering event handlers
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
