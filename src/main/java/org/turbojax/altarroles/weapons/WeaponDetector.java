package org.turbojax.altarroles.weapons;

import org.bukkit.inventory.ItemStack;

public interface WeaponDetector {
    public boolean isHyperion(ItemStack item);
    public boolean isNightPiercer(ItemStack item);
    public boolean isPaleCannon(ItemStack item);

    public default boolean isTeamWeapon(ItemStack item) {
        return isHyperion(item) || isNightPiercer(item) || isPaleCannon(item);
    }
}
