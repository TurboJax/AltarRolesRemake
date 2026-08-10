package org.turbojax.altarroles;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TimeChecker {
    private final AltarRoles plugin = JavaPlugin.getPlugin(AltarRoles.class);

    public void start() {
        List<World> worlds = Bukkit.getWorlds();

        for (World world : worlds) {
            long time = world.getTime();

            // TODO: Start a task for each individual world
            // The task should run at the cusp of day and night
            // Make sure to have the task wait until the next day/night time to start looping but also loop once immediately

            // day start: 0
            // night start: 12000
        }
    }
}
