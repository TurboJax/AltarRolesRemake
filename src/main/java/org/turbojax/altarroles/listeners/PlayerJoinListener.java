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
        PlayerHelper.setRole(player, Role.TEMP_PALE);
        DataManager.removePlayerToRevealRot(player.getUniqueId());
    }
}
