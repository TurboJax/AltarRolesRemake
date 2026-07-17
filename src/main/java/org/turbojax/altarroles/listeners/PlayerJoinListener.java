package org.turbojax.altarroles.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.turbojax.altarroles.DataManager;
import org.turbojax.altarroles.Role;
import org.turbojax.altarroles.util.PlayerHelper;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void revealHiddenRot(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!DataManager.getPlayersToRevealRot().contains(player.getUniqueId())) return;

        PlayerHelper.setHiddenRot(player, false);
        PlayerHelper.setRole(player, Role.TEMP_PALE_ROT);
        DataManager.removePlayerToRevealRot(player.getUniqueId());
    }

    @EventHandler
    public void incrementHumanCount(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPlayedBefore()) return;

        DataManager.setMemberCount(Role.TEMP_HUMAN, DataManager.getMemberCount(Role.TEMP_HUMAN, true) + 1);
    }
}
