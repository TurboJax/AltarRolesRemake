package org.turbojax.altarroles.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.turbojax.altarroles.Role;
import org.turbojax.altarroles.util.PlayerHelper;
import org.turbojax.altarroles.weapons.WeaponManager;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void changeRoles(PlayerDeathEvent event) {
        Player dead = event.getPlayer();
        Player killer = dead.getKiller();

        // Skipping players who died to something other than a player
        if (killer == null) return;

        Role deadRole = PlayerHelper.getRole(dead);
        Role killerRole = PlayerHelper.getRole(killer);

        // Handling players killed by legendary weapons
        ItemStack item = killer.getInventory().getItemInMainHand();
        if (WeaponManager.isTeamWeapon(item)) {
            PlayerHelper.setRole(dead, killerRole.toTrue());
            return;
        }

        // True roles cannot be converted by normal kills
        if (deadRole.isTrue()) return;

        // Humans cannot convert people back to human
        if (killerRole.isHuman()) return;

        // Converting the dead player to the killer's team
        PlayerHelper.setRole(dead, killerRole.toTemp());
    }

    @EventHandler
    public void applyHiddenRot(PlayerDeathEvent event) {
        Player dead = event.getPlayer();

        if (!dead.hasPotionEffect(PotionEffectType.UNLUCK)) return;

        PlayerHelper.setHiddenRot(dead, true);
    }
}
