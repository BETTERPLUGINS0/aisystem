/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataHolder
 *  org.bukkit.persistence.PersistentDataType
 */
package net.advancedplugins.as.impl.utils.nbt;

import net.advancedplugins.as.impl.utils.pdc.PDCHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class NBTapi {
    public static ItemStack addNBTTag(String string, String string2, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PDCHandler.setString((PersistentDataHolder)itemMeta, string, string2);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack addNBTTag(String string, int n, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PDCHandler.setInt((PersistentDataHolder)itemMeta, string, n);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack addNBTTag(String string, long l, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PDCHandler.setLong((PersistentDataHolder)itemMeta, string, l);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean contains(String string, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().equals((Object)Material.AIR)) {
            return false;
        }
        return itemStack.hasItemMeta() && PDCHandler.contains((PersistentDataHolder)itemStack.getItemMeta(), string);
    }

    public static String get(String string, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return null;
        }
        if (itemStack.hasItemMeta() && PDCHandler.hasString((PersistentDataHolder)itemStack.getItemMeta(), string)) {
            return PDCHandler.getString((PersistentDataHolder)itemStack.getItemMeta(), string);
        }
        return null;
    }

    public static Integer getInt(String string, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return null;
        }
        if (PDCHandler.hasInt((PersistentDataHolder)itemStack.getItemMeta(), string)) {
            return PDCHandler.getInt((PersistentDataHolder)itemStack.getItemMeta(), string);
        }
        return 0;
    }

    public static long getLong(String string, ItemStack itemStack) {
        if (itemStack == null) {
            return 0L;
        }
        if (PDCHandler.has((PersistentDataHolder)itemStack.getItemMeta(), string, PersistentDataType.LONG)) {
            return PDCHandler.getLong((PersistentDataHolder)itemStack.getItemMeta(), string);
        }
        return 0L;
    }

    public static ItemStack removeTag(String string, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PDCHandler.remove((PersistentDataHolder)itemMeta, string);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

