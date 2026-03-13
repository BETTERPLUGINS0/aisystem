/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.plugin;

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.FoliaScheduler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateChecker {
    private static final String UPDATE_URL = "https://advancedplugins.net/api/v1/getVersion.php?plugin=";

    public static void checkUpdate(JavaPlugin javaPlugin) {
        FoliaScheduler.runTaskLaterAsynchronously((Plugin)javaPlugin, () -> {
            String string;
            String string2 = javaPlugin.getDescription().getVersion();
            String string3 = javaPlugin.getDescription().getName();
            try {
                string = ASManager.fetchJsonFromUrl(UPDATE_URL + string3);
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
            if (string.isEmpty() || UpdateChecker.isLatest(string2, string)) {
                return;
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&7[" + string3 + "] &eYou're using an outdated version of " + string3 + ". A new version is available: &f" + string)));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&7[" + string3 + "] &7Keep your Advanced plugins up to date automatically with &bMintServers&7 Unlimited Hosting: &bhttps://mintservers.com/")));
        }, 20L);
    }

    private static boolean isLatest(String string, String string2) {
        String[] stringArray = string.split("\\.");
        String[] stringArray2 = string2.split("\\.");
        for (int i = 0; i < Math.min(stringArray.length, stringArray2.length); ++i) {
            int n;
            int n2 = Integer.parseInt(stringArray[i]);
            if (n2 < (n = Integer.parseInt(stringArray2[i]))) {
                return false;
            }
            if (n2 <= n) continue;
            return true;
        }
        return stringArray.length >= stringArray2.length;
    }
}

