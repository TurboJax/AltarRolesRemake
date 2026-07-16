package org.turbojax.altarroles;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.turbojax.altarroles.listeners.PlayerDeathListener;

public final class AltarRoles extends JavaPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger("AltarRoles");

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
