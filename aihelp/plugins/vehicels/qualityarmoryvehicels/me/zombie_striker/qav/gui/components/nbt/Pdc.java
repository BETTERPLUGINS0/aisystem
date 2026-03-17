/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.NamespacedKey
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.components.nbt;

import me.zombie_striker.qav.gui.components.nbt.NbtWrapper;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Pdc
implements NbtWrapper {
    private static final Plugin PLUGIN = JavaPlugin.getProvidingPlugin(Pdc.class);

    @Override
    public ItemStack setString(@NotNull ItemStack itemStack, String string, String string2) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(PLUGIN, string), PersistentDataType.STRING, (Object)string2);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public ItemStack removeTag(@NotNull ItemStack itemStack, String string) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        itemMeta.getPersistentDataContainer().remove(new NamespacedKey(PLUGIN, string));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public ItemStack setBoolean(@NotNull ItemStack itemStack, String string, boolean bl) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(PLUGIN, string), PersistentDataType.BYTE, (Object)(bl ? (byte)1 : 0));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    @Nullable
    public String getString(@NotNull ItemStack itemStack, String string) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return null;
        }
        return (String)itemMeta.getPersistentDataContainer().get(new NamespacedKey(PLUGIN, string), PersistentDataType.STRING);
    }
}

