package org.turbojax.altarroles.util;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.turbojax.altarroles.AltarRoles;
import org.turbojax.altarroles.DataManager;
import org.turbojax.altarroles.MainConfig;
import org.turbojax.altarroles.Role;

public class PlayerHelper {
    public static final NamespacedKey ROLE_KEY = new NamespacedKey("altar_roles", "role");
    public static final NamespacedKey HIDDEN_ROT_KEY = new NamespacedKey("altar_roles", "hidden_rot");
    public static final NamespacedKey ROLE_LOCK_KEY = new NamespacedKey("altar_roles", "lock");

    public static Role getRole(OfflinePlayer player) {
        return player.getPersistentDataContainer().getOrDefault(ROLE_KEY, CustomPDCTypes.ROLE, Role.TEMP_HUMAN);
    }

    public static void setRole(Player player, Role role) {
        // Getting the old role
        Role oldRole = getRole(player);

        // Updating member counts
        DataManager.setMemberCount(oldRole, DataManager.getMemberCount(oldRole, true) - 1);
        DataManager.setMemberCount(role, DataManager.getMemberCount(role, true) + 1);

        // Adding the player to the scoreboard if the team is revealed
        if (MainConfig.isRevealed(role)) {
            AltarRoles.getTeam(role).addPlayer(player);
        }

        // Saving the new role
        player.getPersistentDataContainer().set(ROLE_KEY, CustomPDCTypes.ROLE, role);
    }

    public static boolean hasHiddenRot(OfflinePlayer player) {
        return player.getPersistentDataContainer().has(HIDDEN_ROT_KEY);
    }

    public static void setHiddenRot(Player player, boolean hiddenRot) {
        if (!hiddenRot) {
            player.getPersistentDataContainer().remove(HIDDEN_ROT_KEY);
            return;
        }

        player.getPersistentDataContainer().set(HIDDEN_ROT_KEY, PersistentDataType.BOOLEAN, true);
    }

    public static void lock(Player player) {
        player.getPersistentDataContainer().set(ROLE_LOCK_KEY, PersistentDataType.BOOLEAN, true);
    }

    public static boolean isLocked(OfflinePlayer player) {
        return player.getPersistentDataContainer().has(ROLE_LOCK_KEY);
    }

    public static void unlock(Player player) {
        player.getPersistentDataContainer().remove(ROLE_LOCK_KEY);
    }
}
