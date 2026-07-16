package org.turbojax.altarroles.weapons;

import org.bukkit.inventory.ItemStack;
import org.turbojax.altarroles.AltarRoles;

public class WeaponManager {
    private static WeaponDetector detector;

    public static void setDetecterImpl(WeaponDetector detector) {
        WeaponManager.detector = detector;
    }

    public static boolean isHyperion(ItemStack item) {
        if (detector == null) {
            AltarRoles.LOGGER.warn("No WeaponDetecter instance has been loaded.  Cannot check if the item is a Hyperion.");
            return false;
        }

        return detector.isHyperion(item);
    }

    public static boolean isNightPiercer(ItemStack item) {
        if (detector == null) {
            AltarRoles.LOGGER.warn("No WeaponDetecter instance has been loaded.  Cannot check if the item is a Night Piercer.");
            return false;
        }

        return detector.isNightPiercer(item);
    }

    public static boolean isPaleCannon(ItemStack item) {
        if (detector == null) {
            AltarRoles.LOGGER.warn("No WeaponDetecter instance has been loaded.  Cannot check if the item is a Pale Cannon.");
            return false;
        }

        return detector.isPaleCannon(item);
    }

    public static boolean isTeamWeapon(ItemStack item) {
        if (detector == null) {
            AltarRoles.LOGGER.warn("No WeaponDetecter instance has been loaded.  Cannot check if the item is a Pale Cannon.");
            return false;
        }

        return detector.isTeamWeapon(item);
    }
}
