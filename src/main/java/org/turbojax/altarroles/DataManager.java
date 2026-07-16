package org.turbojax.altarroles;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.turbojax.altarroles.util.PlayerHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataManager {
    public static final AltarRoles plugin = JavaPlugin.getPlugin(AltarRoles.class);
    public static final File file = new File(plugin.getDataFolder(), "data.yml");
    public static final FileConfiguration config = new YamlConfiguration();

    private DataManager() {}

    /**
     * Reloads the configuration.
     *
     * @return Whether the configuration was loaded successfully.
     */
    public static boolean load() {
        // Not doing anything if the plugin isn't enabled
        if (!plugin.isEnabled()) {
            AltarRoles.LOGGER.error("{} not loaded, cannot load {}.", plugin.getName(), file.getName());
            return false;
        }

        // Creating the file if it doesn't exist.
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), true);
        }

        // Loading the config
        try {
            config.load(file);
            AltarRoles.LOGGER.info("Successfully loaded {}", file.getName());
            return true;
        } catch (InvalidConfigurationException e) {
            AltarRoles.LOGGER.warn("{} contains an invalid YAML configuration.  Verify the contents of the file.", file.getName());
        } catch (IOException e) {
            AltarRoles.LOGGER.error("Could not find {}.  Check that it exists.", file.getName());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Writes the config to the file.
     * 
     * @return Whether or not the config was successfully written.
     */
    public static boolean save() {
        // Not doing anything if the plugin isn't enabled
        if (!plugin.isEnabled()) {
            AltarRoles.LOGGER.error("{} not loaded, cannot save {}.", plugin.getName(), file.getName());
            return false;
        }

        // Creating the file if it doesn't exist.
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }

        // Saving the config
        try {
            config.save(file);
            AltarRoles.LOGGER.info("Saved {}", file.getName());
            return true;
        } catch (IOException e) {
            AltarRoles.LOGGER.warn("Could not save {}.  Make sure the user has write permissions.", file.getName());
        }

        return false;
    }

    public static int getMemberCount(Role role, boolean strict) {
        if (strict) {
            return config.getInt("members." + role.name().toLowerCase(), 0);
        }

        return config.getInt("members." + role.toTemp().name().toLowerCase(), 0) + config.getInt("members." + role.toTrue().name().toLowerCase(), 0);
    }

    public static void setMemberCount(Role role, int count) {
        config.set("members." + role.name().toLowerCase(), count);

        save();
    }

    // Doesn't actually require data.yml, but it goes with all the stuff that's in here.
    public static int getOnlineMemberCount(Role role, boolean strict) {
        if (strict) {
            return (int) List.copyOf(Bukkit.getOnlinePlayers()).stream()
                .filter(p -> PlayerHelper.getRole(p) == role)
                .count();
        }

        return (int) List.copyOf(Bukkit.getOnlinePlayers()).stream()
            .filter(p -> PlayerHelper.getRole(p).matchesTeam(role))
            .count();
    }

    public static void applyUpdates() {}
}