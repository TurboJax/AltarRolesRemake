package org.turbojax.altarroles.listeners;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.turbojax.altarroles.util.PlayerHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMoveListener implements Listener {
    private final Set<UUID> mossBuffed = new HashSet<>();
    private final Set<UUID> rainDebuffed = new HashSet<>();
    private final Set<UUID> sunBurning = new HashSet<>();

    @EventHandler
    public void givePaleRot(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (isOnMoss(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, -1, 0));
        } else {
            player.removePotionEffect(PotionEffectType.UNLUCK);
        }
    }

    @EventHandler
    public void checkForMoss(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!PlayerHelper.getRole(player).isPaleRot()) return;

        if (isOnMoss(player) && !mossBuffed.contains(player.getUniqueId())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, -1, 0));

            mossBuffed.add(player.getUniqueId());
        } else if (mossBuffed.contains(player.getUniqueId())) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.STRENGTH);

            mossBuffed.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void checkForRain(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!PlayerHelper.getRole(player).isPaleRot()) return;

        // Giving the player debuffs if they're in rain.  Removing them if they're not
        // TODO: Check that this works for snow
        if (player.isInRain() && !rainDebuffed.contains(player.getUniqueId())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, -1, 0));

            rainDebuffed.add(player.getUniqueId());
        } else if (rainDebuffed.contains(player.getUniqueId())) {
            player.removePotionEffect(PotionEffectType.WEAKNESS);

            rainDebuffed.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void checkForSun(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!PlayerHelper.getRole(player).isVampire()) return;

        if (isInSun(player)) {
            // Technically you can stop burning, but you'd have to sit still for multiple YEARS.
            player.setFireTicks(Integer.MAX_VALUE);

            sunBurning.add(player.getUniqueId());
        } else if (sunBurning.contains(player.getUniqueId())) {
            player.setFireTicks(0);

            sunBurning.remove(player.getUniqueId());
        }
    }

    private boolean isOnMoss(Player player) {
        Material blockBelow = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        Material block2Below = player.getLocation().getBlock().getRelative(BlockFace.DOWN, 2).getType();

        return blockBelow == Material.PALE_MOSS_BLOCK || blockBelow == Material.PALE_MOSS_CARPET || block2Below == Material.PALE_MOSS_BLOCK || block2Below == Material.PALE_MOSS_CARPET;
    }

    private boolean isInSun(Player player) {
        return player.getWorld().isDayTime() && player.getLocation().getBlock().getRelative(BlockFace.UP).getLightFromSky() > 0;
    }
}
