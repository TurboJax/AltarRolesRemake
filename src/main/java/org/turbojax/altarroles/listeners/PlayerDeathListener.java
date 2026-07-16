package org.turbojax.altarroles.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.turbojax.altarroles.DataManager;
import org.turbojax.altarroles.Role;
import org.turbojax.altarroles.util.PlayerHelper;
import org.turbojax.altarroles.weapons.WeaponManager;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getPlayer();
        Player killer = dead.getKiller();

        Role deadRole = PlayerHelper.getRole(dead);
        Role killerRole = PlayerHelper.getRole(killer);

        // Handling players killed by legendary weapons
        ItemStack item = killer.getInventory().getItemInMainHand();
        if (WeaponManager.isTeamWeapon(item)) {
            DataManager.setMemberCount(deadRole, DataManager.getMemberCount(deadRole, true) - 1);
            DataManager.setMemberCount(killerRole, DataManager.getMemberCount(killerRole, true) + 1);
            
            PlayerHelper.setRole(dead, killerRole.toTrue());
            return;
        }

        // True roles cannot be converted by normal kills
        if (!deadRole.isTrue()) return;

        // Humans cannot convert people back to human
        if (killerRole.isHuman()) return;

        DataManager.setMemberCount(deadRole, DataManager.getMemberCount(deadRole, true) - 1);
        DataManager.setMemberCount(killerRole, DataManager.getMemberCount(killerRole, true) + 1);

        // Converting the dead player to the killer's team
        PlayerHelper.setRole(dead, killerRole.toTemp());
    }
}
