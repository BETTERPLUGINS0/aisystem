/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.betterstructures.util;

import java.util.Map;
import org.bukkit.inventory.ItemStack;

public class ItemStackSerialization {
    private ItemStackSerialization() {
    }

    public static Map<String, Object> deserializeItem(ItemStack itemStack) throws IllegalStateException {
        return itemStack.serialize();
    }

    public static ItemStack serializeItem(Map<String, Object> deserializedItemStack) {
        try {
            return ItemStack.deserialize(deserializedItemStack);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

