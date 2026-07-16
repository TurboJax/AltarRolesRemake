package org.turbojax.altarroles;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.turbojax.altarroles.listeners.PlayerDeathListener;

public final class AltarRoles extends JavaPlugin {
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
