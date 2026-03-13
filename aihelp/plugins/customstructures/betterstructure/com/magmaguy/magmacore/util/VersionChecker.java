/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.magmaguy.magmacore.util;

import com.magmaguy.magmacore.MagmaCore;
import com.magmaguy.magmacore.util.ChatColorConverter;
import com.magmaguy.magmacore.util.Logger;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class VersionChecker {
    private static final boolean SHA1Updated = false;
    private static boolean pluginIsUpToDate = true;

    private VersionChecker() {
    }

    public static boolean serverVersionOlderThan(int majorVersion, int minorVersion) {
        String[] splitVersion = Bukkit.getBukkitVersion().split("[.]");
        int actualMajorVersion = Integer.parseInt(splitVersion[1].split("-")[0]);
        int actualMinorVersion = 0;
        if (splitVersion.length > 2) {
            actualMinorVersion = Integer.parseInt(splitVersion[2].split("-")[0]);
        }
        if (actualMajorVersion < majorVersion) {
            return true;
        }
        if (splitVersion.length > 2) {
            return actualMajorVersion == majorVersion && actualMinorVersion < minorVersion;
        }
        return false;
    }

    public static void checkPluginVersion(final String resourceID) {
        new BukkitRunnable(){

            public void run() {
                String currentVersion = MagmaCore.getInstance().getRequestingPlugin().getDescription().getVersion();
                boolean snapshot = false;
                if (currentVersion.contains("SNAPSHOT")) {
                    snapshot = true;
                    currentVersion = currentVersion.split("-")[0];
                }
                String publicVersion = "";
                try {
                    Logger.info("Latest public release is " + VersionChecker.readStringFromURL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID));
                    Logger.info("Your version is " + MagmaCore.getInstance().getRequestingPlugin().getDescription().getVersion());
                    publicVersion = VersionChecker.readStringFromURL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID);
                } catch (IOException e) {
                    Logger.warn("Couldn't check latest version");
                    pluginIsUpToDate = false;
                    return;
                }
                if (Double.parseDouble(currentVersion.split("\\.")[0]) < Double.parseDouble(publicVersion.split("\\.")[0])) {
                    VersionChecker.outOfDateHandler();
                    pluginIsUpToDate = false;
                } else if (Double.parseDouble(currentVersion.split("\\.")[0]) == Double.parseDouble(publicVersion.split("\\.")[0])) {
                    if (Double.parseDouble(currentVersion.split("\\.")[1]) < Double.parseDouble(publicVersion.split("\\.")[1])) {
                        VersionChecker.outOfDateHandler();
                        pluginIsUpToDate = false;
                    } else if (Double.parseDouble(currentVersion.split("\\.")[1]) == Double.parseDouble(publicVersion.split("\\.")[1]) && Double.parseDouble(currentVersion.split("\\.")[2]) < Double.parseDouble(publicVersion.split("\\.")[2])) {
                        VersionChecker.outOfDateHandler();
                        pluginIsUpToDate = false;
                    }
                }
                if (pluginIsUpToDate) {
                    if (!snapshot) {
                        Logger.info("You are running the latest version!");
                    } else {
                        Logger.info("You are running a snapshot version! You can check for updates in the #releases channel on the Nightbreak Discord!");
                    }
                }
            }
        }.runTaskAsynchronously((Plugin)MagmaCore.getInstance().getRequestingPlugin());
    }

    private static String readStringFromURL(String url) throws IOException {
        try (Scanner scanner = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8);){
            scanner.useDelimiter("\\A");
            String string = scanner.hasNext() ? scanner.next() : "";
            return string;
        }
    }

    private static void outOfDateHandler() {
        Logger.warn("A newer version of this plugin is available for download!");
    }

    @Generated
    public static boolean isSHA1Updated() {
        return false;
    }

    public static class VersionCheckerEvents
    implements Listener {
        private static String downloadURL;

        @EventHandler
        public void onPlayerJoinEvent(final PlayerJoinEvent event) {
            new BukkitRunnable(){

                public void run() {
                    if (!event.getPlayer().isOp()) {
                        return;
                    }
                    if (!event.getPlayer().isOnline()) {
                        return;
                    }
                    if (!pluginIsUpToDate) {
                        Logger.sendMessage((CommandSender)event.getPlayer(), ChatColorConverter.convert("&a[" + MagmaCore.getInstance().getRequestingPlugin().getName() + "] &cYour version of " + MagmaCore.getInstance().getRequestingPlugin().getName() + " is outdated. &aYou can download the latest version from &3&n&o" + downloadURL));
                    }
                }
            }.runTaskLater((Plugin)MagmaCore.getInstance().getRequestingPlugin(), 60L);
        }

        @Generated
        public static String getDownloadURL() {
            return downloadURL;
        }

        @Generated
        public static void setDownloadURL(String downloadURL) {
            VersionCheckerEvents.downloadURL = downloadURL;
        }
    }
}

