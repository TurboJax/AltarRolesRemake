package org.turbojax.altarroles.weapons.impl;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.turbojax.altarroles.weapons.WeaponDetector;

public class OfficialWeaponDetectorImpl implements WeaponDetector {
    private static final NamespacedKey HYPERION_MODEL = new NamespacedKey("altarsmp", "weapons/s1_a2/hyperion");
    private static final NamespacedKey NIGHTPIERCER__MODEL = new NamespacedKey("altarsmp", "weapons/s1_a2/nightpiercer");
    private static final NamespacedKey PALE_CANNON_MODEL = new NamespacedKey("altarsmp", "weapons/s1_a2/pale_cannon");

    /**
     * Checks if an item has the specified model.
     * 
     * @param item An {@link ItemStack} to check for a model on.
     * @param modelKey The model to look for.
     * 
     * @return true if the item has the model, false otherwise
     */
    private boolean compareModels(ItemStack item, NamespacedKey modelKey) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        if (!meta.hasItemModel()) return false;

        return meta.getItemModel().equals(modelKey);
    }

    @Override
    public boolean isHyperion(ItemStack item) {
        return compareModels(item, HYPERION_MODEL);
    }

    @Override
    public boolean isNightpiercer(ItemStack item) {
        return compareModels(item, NIGHTPIERCER__MODEL);
    }

    @Override
    public boolean isPaleCannon(ItemStack item) {
        return compareModels(item, PALE_CANNON_MODEL);
    }

}