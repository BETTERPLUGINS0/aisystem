/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.NamespacedKey
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataHolder
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.Nullable
 */
package net.advancedplugins.as.impl.utils.pdc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class PDCHandler {
    private static final List<PersistentDataType> dataTypes = Arrays.asList(PersistentDataType.BYTE, PersistentDataType.BYTE_ARRAY, PersistentDataType.DOUBLE, PersistentDataType.FLOAT, PersistentDataType.INTEGER, PersistentDataType.INTEGER_ARRAY, PersistentDataType.LONG, PersistentDataType.SHORT, PersistentDataType.STRING);

    public static boolean contains(PersistentDataHolder persistentDataHolder, String string) {
        if (persistentDataHolder == null) {
            return false;
        }
        return dataTypes.stream().filter(persistentDataType -> persistentDataHolder.getPersistentDataContainer().has(PDCHandler.getNamespace(string), persistentDataType)).findFirst().orElse(null) != null;
    }

    public static String getString(PersistentDataHolder persistentDataHolder, String string, String string2) {
        if (!PDCHandler.has(persistentDataHolder, string, PersistentDataType.STRING)) {
            return string2;
        }
        return PDCHandler.get(persistentDataHolder, string, PersistentDataType.STRING).toString();
    }

    public static String getString(PersistentDataHolder persistentDataHolder, String string) {
        if (!PDCHandler.has(persistentDataHolder, string, PersistentDataType.STRING)) {
            return null;
        }
        return PDCHandler.get(persistentDataHolder, string, PersistentDataType.STRING).toString();
    }

    public static int getInt(PersistentDataHolder persistentDataHolder, String string) {
        if (!PDCHandler.has(persistentDataHolder, string, PersistentDataType.INTEGER)) {
            return 0;
        }
        return (Integer)PDCHandler.get(persistentDataHolder, string, PersistentDataType.INTEGER);
    }

    public static long getLong(PersistentDataHolder persistentDataHolder, String string) {
        if (!PDCHandler.has(persistentDataHolder, string, PersistentDataType.LONG)) {
            return 0L;
        }
        return (Long)PDCHandler.get(persistentDataHolder, string, PersistentDataType.LONG);
    }

    public static float getFloat(PersistentDataHolder persistentDataHolder, String string) {
        if (!PDCHandler.has(persistentDataHolder, string, PersistentDataType.FLOAT)) {
            return 0.0f;
        }
        return ((Float)PDCHandler.get(persistentDataHolder, string, PersistentDataType.FLOAT)).floatValue();
    }

    public static double getDouble(PersistentDataHolder persistentDataHolder, String string) {
        if (!PDCHandler.has(persistentDataHolder, string, PersistentDataType.DOUBLE)) {
            return 0.0;
        }
        return (Double)PDCHandler.get(persistentDataHolder, string, PersistentDataType.DOUBLE);
    }

    public static boolean getBoolean(PersistentDataHolder persistentDataHolder, String string) {
        if (!PDCHandler.has(persistentDataHolder, string, PersistentDataType.BYTE)) {
            return false;
        }
        return (Byte)PDCHandler.get(persistentDataHolder, string, PersistentDataType.BYTE) == 1;
    }

    public static void set(PersistentDataHolder persistentDataHolder, String string, PersistentDataType persistentDataType, Object object) {
        if (persistentDataHolder == null) {
            return;
        }
        persistentDataHolder.getPersistentDataContainer().set(PDCHandler.getNamespace(string), persistentDataType, object);
    }

    public static void remove(PersistentDataHolder persistentDataHolder, String string) {
        persistentDataHolder.getPersistentDataContainer().remove(PDCHandler.getNamespace(string));
    }

    public static Object get(PersistentDataHolder persistentDataHolder, String string, PersistentDataType persistentDataType) {
        if (persistentDataHolder instanceof ItemStack && !((ItemStack)persistentDataHolder).hasItemMeta()) {
            return null;
        }
        return persistentDataHolder.getPersistentDataContainer().get(PDCHandler.getNamespace(string), persistentDataType);
    }

    public static boolean has(PersistentDataHolder persistentDataHolder, String string, PersistentDataType persistentDataType) {
        if (persistentDataHolder instanceof ItemStack && !((ItemStack)persistentDataHolder).hasItemMeta()) {
            return false;
        }
        if (persistentDataHolder == null) {
            return false;
        }
        return persistentDataHolder.getPersistentDataContainer().has(PDCHandler.getNamespace(string), persistentDataType);
    }

    public static boolean has(PersistentDataHolder persistentDataHolder, String string) {
        if (persistentDataHolder instanceof ItemStack && !((ItemStack)persistentDataHolder).hasItemMeta()) {
            return false;
        }
        if (persistentDataHolder == null) {
            return false;
        }
        return persistentDataHolder.getPersistentDataContainer().has(PDCHandler.getNamespace(string));
    }

    public static boolean has(PersistentDataHolder persistentDataHolder, @Nullable NamespacedKey namespacedKey) {
        if (namespacedKey == null) {
            return false;
        }
        if (persistentDataHolder instanceof ItemStack && !((ItemStack)persistentDataHolder).hasItemMeta()) {
            return false;
        }
        if (persistentDataHolder == null) {
            return false;
        }
        return persistentDataHolder.getPersistentDataContainer().has(namespacedKey);
    }

    public static boolean hasString(PersistentDataHolder persistentDataHolder, String string) {
        return PDCHandler.has(persistentDataHolder, string, PersistentDataType.STRING);
    }

    public static boolean hasInt(PersistentDataHolder persistentDataHolder, String string) {
        return PDCHandler.has(persistentDataHolder, string, PersistentDataType.INTEGER);
    }

    public static boolean hasBoolean(PersistentDataHolder persistentDataHolder, String string) {
        return PDCHandler.has(persistentDataHolder, string, PersistentDataType.BYTE);
    }

    @Nullable
    public static NamespacedKey getNamespace(String string, String string2) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(string);
        if (plugin == null) {
            return null;
        }
        return new NamespacedKey(plugin, string2);
    }

    public static NamespacedKey getNamespace(String string) {
        return new NamespacedKey((Plugin)ASManager.getInstance(), string.replace(";", "-"));
    }

    public static void setString(PersistentDataHolder persistentDataHolder, String string, String string2) {
        PDCHandler.set(persistentDataHolder, string, PersistentDataType.STRING, (Object)string2);
    }

    public static void setBoolean(PersistentDataHolder persistentDataHolder, String string, boolean bl) {
        PDCHandler.set(persistentDataHolder, string, PersistentDataType.BYTE, (byte)(bl ? 1 : 0));
    }

    public static void setLong(PersistentDataHolder persistentDataHolder, String string, long l) {
        PDCHandler.set(persistentDataHolder, string, PersistentDataType.LONG, l);
    }

    public static void setDouble(PersistentDataHolder persistentDataHolder, String string, double d) {
        PDCHandler.set(persistentDataHolder, string, PersistentDataType.DOUBLE, d);
    }

    public static void setInt(PersistentDataHolder persistentDataHolder, String string, int n) {
        PDCHandler.set(persistentDataHolder, string, PersistentDataType.INTEGER, n);
    }

    public static long getLong(World world, String string) {
        return (Long)PDCHandler.get((PersistentDataHolder)world, string, PersistentDataType.LONG);
    }

    public static void unset(PersistentDataHolder persistentDataHolder, String string) {
        if (persistentDataHolder == null) {
            return;
        }
        persistentDataHolder.getPersistentDataContainer().remove(PDCHandler.getNamespace(string));
    }

    public static boolean hasPDC(PersistentDataHolder persistentDataHolder) {
        if (persistentDataHolder == null) {
            return false;
        }
        return !persistentDataHolder.getPersistentDataContainer().getKeys().isEmpty();
    }

    public static List<String> getKeys(ItemStack itemStack) {
        if (itemStack == null) {
            return Collections.emptyList();
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Collections.emptyList();
        }
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        Set set = persistentDataContainer.getKeys();
        if (set.isEmpty()) {
            return Collections.emptyList();
        }
        if (set.size() == 1) {
            return Collections.singletonList(((NamespacedKey)set.iterator().next()).getKey());
        }
        ArrayList<String> arrayList = new ArrayList<String>(set.size());
        for (NamespacedKey namespacedKey : set) {
            arrayList.add(namespacedKey.getKey());
        }
        return Collections.unmodifiableList(arrayList);
    }

    public static Object get(ItemStack itemStack, NamespacedKey namespacedKey) {
        for (PersistentDataType persistentDataType : dataTypes) {
            if (!itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, persistentDataType)) continue;
            return itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, persistentDataType);
        }
        return null;
    }

    public static void set(Block block, String string, PersistentDataType persistentDataType, String string2) {
        block.getChunk().getPersistentDataContainer().set(PDCHandler.getNamespace(PDCHandler.blockToString(block) + string), persistentDataType, (Object)string2);
    }

    public static Object get(Block block, String string) {
        for (PersistentDataType persistentDataType : dataTypes) {
            if (!block.getChunk().getPersistentDataContainer().has(PDCHandler.getNamespace(PDCHandler.blockToString(block) + string), persistentDataType)) continue;
            return block.getChunk().getPersistentDataContainer().get(PDCHandler.getNamespace(PDCHandler.blockToString(block) + string), persistentDataType);
        }
        return null;
    }

    public static void remove(Block block, String string) {
        block.getChunk().getPersistentDataContainer().remove(PDCHandler.getNamespace(PDCHandler.blockToString(block) + string));
    }

    public static boolean has(Block block, String string) {
        for (PersistentDataType persistentDataType : dataTypes) {
            if (!block.getChunk().getPersistentDataContainer().has(PDCHandler.getNamespace(PDCHandler.blockToString(block) + string), persistentDataType)) continue;
            return true;
        }
        return false;
    }

    private static String blockToString(Block block) {
        return block.getX() + ";" + block.getY() + ";" + block.getZ() + ";";
    }

    public static String getKeys(Player player) {
        return player.getPersistentDataContainer().getKeys().stream().map(NamespacedKey::getKey).collect(Collectors.joining(","));
    }
}

