/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.infrastructure.utils;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TextUtils {
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)text);
    }

    @Deprecated
    public static String licenseReplacer(String license) {
        if (license.split("_").length > 1) {
            return license.split("_")[2];
        }
        return null;
    }

    public static List<String> list(String ... strings) {
        return Arrays.asList(strings);
    }

    public static boolean checkInvFull(Player player) {
        return !Arrays.asList(player.getInventory().getStorageContents()).contains(null);
    }
}

