/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.SkullType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package me.zombie_striker.qav;

import java.util.Arrays;
import java.util.List;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.attachments.Attachment;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemFact {
    public static Material skull;

    public static void init() {
        skull = Material.matchMaterial((String)"PLAYER_HEAD");
        if (skull == null) {
            skull = Material.matchMaterial((String)"SKULL_ITEM");
        }
    }

    public static ItemStack askull(String string, String string2, String ... stringArray) {
        boolean bl = skull.name().equals("SKULL_ITEM");
        ItemStack itemStack = ItemFact.a(skull, bl ? SkullType.PLAYER.ordinal() : 0, string2, stringArray);
        if (string != null || bl) {
            SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
            try {
                skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer((String)string));
            } catch (Error | Exception throwable) {
                skullMeta.setOwner(string);
            }
            itemStack.setItemMeta((ItemMeta)skullMeta);
        }
        return itemStack;
    }

    public static ItemStack a(Material material, String string, String ... stringArray) {
        return ItemFact.a(material, 0, string, stringArray);
    }

    public static ItemStack getItem(AbstractVehicle abstractVehicle) {
        if (abstractVehicle != null) {
            return ItemFact.getItem(abstractVehicle.getDisplayname(), abstractVehicle.getLore(), abstractVehicle.getMaterial(), abstractVehicle.getItemData());
        }
        return null;
    }

    public static ItemStack getItem(Attachment attachment) {
        if (attachment != null) {
            return ItemFact.getItem(attachment.getName(), null, attachment.getMaterial(), attachment.getId());
        }
        return null;
    }

    public static ItemStack getItem(AbstractVehicle abstractVehicle, Material material) {
        return ItemFact.getItem(abstractVehicle.getDisplayname(), abstractVehicle.getLore(), material, abstractVehicle.getItemData());
    }

    public static ItemStack getItem(String string, List<String> list, Material material, int n) {
        ItemStack itemStack = null;
        ItemMeta itemMeta = null;
        if (Main.useDamage) {
            itemStack = new ItemStack(material, 1, (short)n);
            itemMeta = itemStack.getItemMeta();
        } else {
            try {
                itemStack = new ItemStack(material);
                itemMeta = itemStack.getItemMeta();
                itemMeta.setCustomModelData(Integer.valueOf(n));
            } catch (Error | Exception throwable) {
                itemStack = new ItemStack(material, 1, (short)n);
                itemMeta = itemStack.getItemMeta();
            }
        }
        if (string != null) {
            itemMeta.setDisplayName(ChatColor.GOLD + string);
            if (list != null) {
                itemMeta.setLore(list);
            }
        }
        try {
            itemMeta.setUnbreakable(true);
        } catch (Error | Exception throwable) {
            // empty catch block
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack a(Material material, int n, String string, String ... stringArray) {
        return ItemFact.a(material, n, false, string, stringArray);
    }

    public static ItemStack a(Material material, int n, boolean bl, String string, String ... stringArray) {
        ItemStack itemStack = null;
        ItemMeta itemMeta = null;
        if (Main.useDamage) {
            itemStack = new ItemStack(material, 1, (short)n);
            itemMeta = itemStack.getItemMeta();
        } else {
            try {
                itemStack = new ItemStack(material);
                itemMeta = itemStack.getItemMeta();
                itemMeta.setCustomModelData(Integer.valueOf(n));
            } catch (Error | Exception throwable) {
                itemStack = new ItemStack(material, 1, (short)n);
                itemMeta = itemStack.getItemMeta();
            }
        }
        itemMeta.setDisplayName(string);
        if (stringArray != null) {
            itemMeta.setLore(Arrays.asList(stringArray));
        }
        try {
            itemMeta.setUnbreakable(bl);
        } catch (Error | Exception throwable) {
            try {
                itemMeta.setUnbreakable(bl);
            } catch (Error | Exception throwable2) {
                // empty catch block
            }
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

