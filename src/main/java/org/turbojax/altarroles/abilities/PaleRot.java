package org.turbojax.altarroles.abilities;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.turbojax.altarroles.Role;
import org.turbojax.altarroles.util.PlayerHelper;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;

public class PaleRot {
    private static final PotionEffect PASSIVE_WEAKNESS = new PotionEffect(PotionEffectType.WEAKNESS, -1, 0);
    private static final PotionEffect PASSIVE_STRENGTH = new PotionEffect(PotionEffectType.STRENGTH, -1, 0);
    private static final PotionEffect PASSIVE_SPEED = new PotionEffect(PotionEffectType.SPEED, -1, 0);

    private static final PotionEffect ROTTEN_REGEN = new PotionEffect(PotionEffectType.REGENERATION, 100, 0);
    private static final PotionEffect ROTTEN_ABSORPTION = new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0);

    public void applyPassives(Player player) {
        // TODO: Handle removing effects
        if (player.isInRain()) {
            player.addPotionEffect(PASSIVE_WEAKNESS);
        }

        Material m1 = player.getLocation().getBlock().getType();
        Material m2 = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if (m1 == Material.PALE_MOSS_CARPET || m1 == Material.PALE_MOSS_BLOCK || m2 == Material.PALE_MOSS_CARPET || m2 == Material.PALE_MOSS_BLOCK) {
            player.addPotionEffect(PASSIVE_STRENGTH);
            player.addPotionEffect(PASSIVE_SPEED);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        Role role = PlayerHelper.getRole(player);
        if (!role.isPale()) return;
        
        if (!(event.getEntity() instanceof Player target)) return;

        Location targetEyeLoc = target.getEyeLocation();
        Location playerEyeLoc = player.getEyeLocation();

        Vector targetLookDir = targetEyeLoc.getDirection();
        Vector toTarget = playerEyeLoc.toVector().subtract(targetEyeLoc.toVector()).normalize();

        double dotProduct = targetLookDir.dot(toTarget);

        // 180 degree FOV
        double fovThreshold = Math.cos(Math.PI / 2);

        // if withing 180 degree fov, skip dmg boost
        if (dotProduct <= fovThreshold) return;

        event.setDamage(event.getDamage() * 1.5);
    }

    @EventHandler
    public void preventCreakingTargeting(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;

        Role role = PlayerHelper.getRole(player);
        if (!role.isPale()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void rottenFleshPowers(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Role role = PlayerHelper.getRole(player);
        if (!role.isPale()) return;

        player.addPotionEffect(ROTTEN_ABSORPTION);
        player.addPotionEffect(ROTTEN_REGEN);

        ItemStack item = event.getItem();
        if (item.getType() != Material.ROTTEN_FLESH) return;

        // TODO: Check that the item is consumed ingame

        // Setting the replacement to an unmodified rotten flesh
        if (item.getAmount() == 1) {
            event.setReplacement(new ItemStack(Material.AIR));
        } else {
            event.setReplacement(item.clone().subtract(1));
        }

        // Getting item data
        Consumable consumable = item.getData(DataComponentTypes.CONSUMABLE);
        FoodProperties food = item.getData(DataComponentTypes.FOOD);

        // Modifying item data
        food = food.toBuilder()
            .nutrition(4)
            .saturation(10)
            .build();

        ConsumeEffect.applyStatusEffects(List.of(), 1);
        consumable = consumable.toBuilder()
            .effects(List.of())
            .build();

        // Updating item data
        item.setData(DataComponentTypes.CONSUMABLE, consumable);
        item.setData(DataComponentTypes.FOOD, food);

        event.setItem(item);
    }
}
