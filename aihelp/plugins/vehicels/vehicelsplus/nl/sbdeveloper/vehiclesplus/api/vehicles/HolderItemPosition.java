/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.EulerAngle
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public enum HolderItemPosition {
    HEAD,
    MAIN_HAND,
    OFF_HAND;


    public void setItem(ArmorStand armorStand, ItemStack itemStack) {
        switch (this.ordinal()) {
            case 0: {
                armorStand.getEquipment().setHelmet(itemStack);
                break;
            }
            case 1: {
                armorStand.getEquipment().setItemInMainHand(itemStack);
                armorStand.setRightArmPose(new EulerAngle(0.0, 0.0, 0.0));
                break;
            }
            case 2: {
                armorStand.getEquipment().setItemInOffHand(itemStack);
                armorStand.setLeftArmPose(new EulerAngle(0.0, 0.0, 0.0));
            }
        }
    }
}

