package org.turbojax.altarroles.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void checkForMoss(PlayerMoveEvent event) {
        Location dest = event.getTo();
        Player player = event.getPlayer();

        Material blockBelow = dest.getBlock().getRelative(BlockFace.DOWN).getType();
        Material block2Below = dest.getBlock().getRelative(BlockFace.DOWN, 2).getType();

        if (blockBelow == Material.PALE_MOSS_BLOCK || blockBelow == Material.PALE_MOSS_CARPET || block2Below == Material.PALE_MOSS_BLOCK || block2Below == Material.PALE_MOSS_CARPET) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, -1, 0));
        } else {
            player.removePotionEffect(PotionEffectType.UNLUCK);
        }
    }
}
