package org.turbojax.altarroles.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.turbojax.altarroles.Role;
import org.turbojax.altarroles.util.PlayerHelper;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getPlayer();
        Player killer = dead.getKiller();

        Role deadRole = PlayerHelper.getRole(dead);
        Role killerRole = PlayerHelper.getRole(killer);

        // TODO: Handle legendaries
        // Should legendary kills convert the player to the wielder's team or the weapon's team?
        // Probably the wielder's team.

        // True roles cannot be converted by normal kills
        if (!deadRole.isTrue()) return;

        // Humans cannot convert people back to human
        if (killerRole.isHuman()) return;

        // Converting the dead player to the killer's team
        PlayerHelper.setRole(dead, killerRole.toTemp());
    }
}
