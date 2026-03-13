/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.actions.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public enum RollItemType {
    HAND(EquipmentSlot.HAND),
    OFFHAND(EquipmentSlot.OFF_HAND),
    HELMET(EquipmentSlot.HEAD),
    CHESTPLATE(EquipmentSlot.CHEST),
    LEGGINGS(EquipmentSlot.LEGS),
    BOOTS(EquipmentSlot.FEET),
    OTHER(new EquipmentSlot[]{null}),
    HANDS(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND),
    MAIN(EquipmentSlot.HAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET),
    GET_FOR_DEATH(new EquipmentSlot[]{null}),
    ALL(new EquipmentSlot[]{null});

    private final EquipmentSlot[] slots;

    private RollItemType(EquipmentSlot ... equipmentSlotArray) {
        this.slots = equipmentSlotArray;
    }

    public static RollItemType getFromEquipment(EquipmentSlot equipmentSlot) {
        for (RollItemType rollItemType : RollItemType.values()) {
            if (!rollItemType.getSlot().equals((Object)equipmentSlot) || rollItemType.slots.length != 1) continue;
            return rollItemType;
        }
        return null;
    }

    public static RollItemType getHand(Player player, ItemStack itemStack) {
        if (player.getInventory().getItemInOffHand().equals((Object)itemStack)) {
            return OFFHAND;
        }
        return HAND;
    }

    public EquipmentSlot getSlot() {
        return this.slots[0];
    }
}

