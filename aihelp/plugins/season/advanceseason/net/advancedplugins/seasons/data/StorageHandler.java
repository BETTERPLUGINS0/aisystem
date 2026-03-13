/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.persistence.PersistentDataHolder
 *  org.bukkit.persistence.PersistentDataType
 */
package net.advancedplugins.seasons.data;

import net.advancedplugins.as.impl.utils.pdc.PDCHandler;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.utils.PDCKey;
import org.bukkit.Bukkit;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class StorageHandler {
    public static Season getSeason(String string) {
        String string2 = PDCHandler.getString((PersistentDataHolder)Bukkit.getWorld((String)string), PDCKey.WORLD_SEASON.getKey());
        if (string2 == null) {
            string2 = "SPRING";
        }
        return Season.valueOf(string2);
    }

    public static void setSeason(Season season, String string) {
        PDCHandler.setString((PersistentDataHolder)Bukkit.getWorld((String)string), PDCKey.WORLD_SEASON.getKey(), season.name());
    }

    public static int getYearDay(String string) {
        return PDCHandler.getInt((PersistentDataHolder)Bukkit.getWorld((String)string), PDCKey.YEAR_DAY.getKey());
    }

    public static int getTransitionTime(String string) {
        return PDCHandler.getInt((PersistentDataHolder)Bukkit.getWorld((String)string), PDCKey.TRANSITION_TIME.getKey());
    }

    public static void setYearDay(int n, String string) {
        PDCHandler.set((PersistentDataHolder)Bukkit.getWorld((String)string), PDCKey.YEAR_DAY.getKey(), PersistentDataType.INTEGER, n);
    }

    public static void setTransitionTime(int n, String string) {
        PDCHandler.set((PersistentDataHolder)Bukkit.getWorld((String)string), PDCKey.TRANSITION_TIME.getKey(), PersistentDataType.INTEGER, n);
    }
}

