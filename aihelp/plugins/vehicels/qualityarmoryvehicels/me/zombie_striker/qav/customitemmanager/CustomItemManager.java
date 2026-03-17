/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.zombie_striker.qav.customitemmanager;

import java.util.HashMap;
import java.util.Set;
import me.zombie_striker.qav.customitemmanager.AbstractItem;
import me.zombie_striker.qav.customitemmanager.pack.ResourcepackProvider;
import me.zombie_striker.qav.customitemmanager.pack.StaticPackProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomItemManager {
    private static ResourcepackProvider resourcepackProvider = null;
    private static HashMap<String, AbstractItem> customItemTypes = new HashMap();

    @Deprecated
    public static String getResourcepack() {
        return resourcepackProvider.getFor(null);
    }

    @Deprecated
    public static void setResourcepack(String string) {
        resourcepackProvider = new StaticPackProvider(string);
    }

    public static String getResourcepack(Player player) {
        return resourcepackProvider.getFor(player);
    }

    public static void setResourcepack(ResourcepackProvider resourcepackProvider) {
        CustomItemManager.resourcepackProvider = resourcepackProvider;
    }

    public static ResourcepackProvider getResourcepackProvider() {
        return resourcepackProvider;
    }

    public static Set<String> getCustomItemTypes() {
        return customItemTypes.keySet();
    }

    public static void registerItemType(String string, AbstractItem abstractItem) {
        customItemTypes.put(string, abstractItem);
    }

    public static AbstractItem getItemType(String string) {
        return customItemTypes.get(string);
    }

    public static boolean isUsingCustomData() {
        try {
            new ItemStack(Material.DIAMOND_BLOCK).getItemMeta().hasCustomModelData();
            return true;
        } catch (Error | Exception throwable) {
            return false;
        }
    }
}

