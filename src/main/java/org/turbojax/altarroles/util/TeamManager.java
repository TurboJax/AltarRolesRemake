package org.turbojax.altarroles.util;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.turbojax.altarroles.Team;
import org.turbojax.altarroles.TeamState;

public class TeamManager {
    public static final NamespacedKey TEAM_KEY = new NamespacedKey("altar_roles", "team");
    public static final NamespacedKey TEAM_STATE_KEY = new NamespacedKey("altar_roles", "team_state");
    public static final NamespacedKey HIDDEN_ROT_KEY = new NamespacedKey("altar_roles", "hidden_rot");

    public static Team getTeam(OfflinePlayer player) {
        return player.getPersistentDataContainer().getOrDefault(TEAM_KEY, CustomPDCTypes.TEAM, Team.HUMAN);
    }

    public static void setTeam(Player player, Team team) {
        player.getPersistentDataContainer().set(TEAM_KEY, CustomPDCTypes.TEAM, team);
    }

    public static TeamState getTeamState(OfflinePlayer player) {
        return player.getPersistentDataContainer().getOrDefault(TEAM_STATE_KEY, CustomPDCTypes.TEAM_STATE, TeamState.TEMP);
    }

    public static void setTeamState(Player player, TeamState teamState) {
        player.getPersistentDataContainer().set(TEAM_STATE_KEY, CustomPDCTypes.TEAM_STATE, teamState);
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
}
