package org.turbojax.altarroles.weapons.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.turbojax.altarroles.weapons.WeaponDetector;

public class DoomwenWeaponDetectorImpl implements WeaponDetector {
    @Override
    public boolean isHyperion(ItemStack item) {
        if (item.getType() != Material.NETHERITE_SWORD) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        if (!meta.hasCustomModelDataComponent()) return false;

        return meta.getCustomModelDataComponent().getFloats().contains(6f);
    }

    @Override
    public boolean isNightpiercer(ItemStack item) {
        if (item.getType() != Material.NETHERITE_SWORD) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        if (!meta.hasCustomModelDataComponent()) return false;

        return meta.getCustomModelDataComponent().getFloats().contains(5f);
    }

    @Override
    public boolean isPaleCannon(ItemStack item) {
        if (item.getType() != Material.CROSSBOW) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        if (!meta.hasCustomModelDataComponent()) return false;

        return meta.getCustomModelDataComponent().getFloats().contains(2f);
    }
    
}