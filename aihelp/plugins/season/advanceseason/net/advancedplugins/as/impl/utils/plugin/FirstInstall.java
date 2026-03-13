/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.utils.plugin;

import java.io.File;
import net.advancedplugins.as.impl.utils.FoliaScheduler;
import net.advancedplugins.as.impl.utils.plugin.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FirstInstall
implements Listener {
    private static boolean announce = false;
    private static String addonURL;
    private static String pluginName;
    private static JavaPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        if (playerJoinEvent.getPlayer().isOp() || playerJoinEvent.getPlayer().hasPermission("advancedplugins.admin")) {
            announce = false;
            FoliaScheduler.runTaskLater((Plugin)plugin, () -> {
                playerJoinEvent.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&fThank you for installing &b&l" + pluginName + "&f!")));
                playerJoinEvent.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&7- &fNeed help? Join our community: &bhttps://discord.gg/advancedplugins"));
                playerJoinEvent.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&7- &fEnhance your experience with our UI overhaul by installing: &b" + addonURL)));
                playerJoinEvent.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&7- &fLooking for the best Minecraft hosting? &aMintServers&f offers &aUNLIMITED RAM&f and top-notch performance: &bhttps://mintservers.com&f"));
            }, 20L);
            PlayerJoinEvent.getHandlerList().unregister((Listener)this);
        }
    }

    public static void checkFirstInstall(JavaPlugin javaPlugin, String string, String string2) {
        FirstInstall.checkFirstInstall(javaPlugin, string, string2, null);
    }

    public static void checkFirstInstall(JavaPlugin javaPlugin, String string, String string2, String string3) {
        UpdateChecker.checkUpdate(javaPlugin);
        if (new File(javaPlugin.getDataFolder(), string).exists()) {
            return;
        }
        announce = true;
        pluginName = javaPlugin.getName();
        addonURL = string2;
        plugin = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)new FirstInstall(), (Plugin)javaPlugin);
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&fThank you for installing &b&l" + javaPlugin.getName() + "&f!")));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&7- &fNeed help? Join our community: &bhttps://discord.gg/advancedplugins"));
        if (string3 == null) {
            if (string2 != null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&7- &fEnhance your experience with our UI overhaul by installing: &b" + string2)));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)string3));
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&7- &fLooking for the best Minecraft hosting? &aMintServers&f offers &aUNLIMITED RAM&f and top-notch performance: &bhttps://mintservers.com&f"));
    }

    public static void sendStartupAlert(JavaPlugin javaPlugin, String string) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&a>> &f" + string)));
    }
}

