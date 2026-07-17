package org.turbojax.altarroles;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.io.File;
import java.io.IOException;

public class MainConfig {
    public static final AltarRoles plugin = JavaPlugin.getPlugin(AltarRoles.class);
    public static final File file = new File(plugin.getDataFolder(), "config.yml");
    public static final FileConfiguration config = new YamlConfiguration();

    private MainConfig() {}

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
            plugin.saveResource(file.getName(), false);
            return true;
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

    public static String altarPlugin() {
        return config.getString("altar_plugin", "official").toLowerCase();
    }

    public static TextColor getColor(Role role) {
        String key = role.name().toLowerCase();
        key = key.substring(5) + "." + key.substring(0, 5) + "color";
        
        String color = config.getString(key);

        // Handle hex codes
        if (color.startsWith("#")) return TextColor.fromHexString(color);

        // Handling named text colors
        return NamedTextColor.NAMES.value(key);
    }

    public static boolean isRevealed(Role role) {
        String key = role.name().toLowerCase().substring(5) + ".revealed";

        return config.getBoolean(key, !role.isPale());
    }

    public static void setRevealed(Role role, boolean revealed) {
        String key = role.name().toLowerCase().substring(5) + ".revealed";

        config.set(key, revealed);

        save();
    }

    public static void applyUpdates() {}
}