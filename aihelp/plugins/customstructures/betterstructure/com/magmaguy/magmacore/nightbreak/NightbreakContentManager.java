/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.nightbreak;

import com.magmaguy.magmacore.MagmaCore;
import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class NightbreakContentManager {
    private static final Map<String, NightbreakAccount.AccessInfo> accessCache = new ConcurrentHashMap<String, NightbreakAccount.AccessInfo>();
    private static final Map<String, NightbreakAccount.VersionInfo> versionCache = new ConcurrentHashMap<String, NightbreakAccount.VersionInfo>();
    private static long lastCacheRefresh = 0L;
    private static final long CACHE_TTL_MS = 300000L;

    public static boolean isCacheStale() {
        return System.currentTimeMillis() - lastCacheRefresh > 300000L;
    }

    public static void clearCache() {
        accessCache.clear();
        versionCache.clear();
        lastCacheRefresh = 0L;
    }

    public static void refreshVersionCache() {
        NightbreakContentManager.refreshVersionCache(MagmaCore.getInstance().getRequestingPlugin());
    }

    public static void refreshVersionCache(JavaPlugin ownerPlugin) {
        if (!NightbreakAccount.hasToken()) {
            return;
        }
        Map<String, NightbreakAccount.VersionInfo> versions = NightbreakAccount.getInstance().getAllVersions();
        if (!versions.isEmpty()) {
            versionCache.clear();
            versionCache.putAll(versions);
            lastCacheRefresh = System.currentTimeMillis();
            Logger.info("Refreshed Nightbreak version cache with " + versions.size() + " entries");
        }
    }

    public static void checkAccessAsync(String slug, Consumer<NightbreakAccount.AccessInfo> callback) {
        NightbreakContentManager.checkAccessAsync(MagmaCore.getInstance().getRequestingPlugin(), slug, callback);
    }

    public static void checkAccessAsync(JavaPlugin ownerPlugin, String slug, Consumer<NightbreakAccount.AccessInfo> callback) {
        if (!NightbreakAccount.hasToken()) {
            callback.accept(null);
            return;
        }
        if (accessCache.containsKey(slug) && !NightbreakContentManager.isCacheStale()) {
            callback.accept(accessCache.get(slug));
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)ownerPlugin, () -> {
            NightbreakAccount.AccessInfo info = NightbreakAccount.getInstance().checkAccess(slug);
            if (info != null) {
                accessCache.put(slug, info);
            }
            Bukkit.getScheduler().runTask((Plugin)ownerPlugin, () -> callback.accept(info));
        });
    }

    public static void downloadAsync(String slug, File destinationFolder, Player player, Consumer<Boolean> onComplete) {
        NightbreakContentManager.downloadAsync(MagmaCore.getInstance().getRequestingPlugin(), slug, destinationFolder, player, onComplete);
    }

    public static void downloadAsync(JavaPlugin ownerPlugin, String slug, File destinationFolder, Player player, Consumer<Boolean> onComplete) {
        if (!NightbreakAccount.hasToken()) {
            if (player != null && player.isOnline()) {
                player.sendMessage("\u00a7c[Nightbreak] No token registered. Use /nightbreaklogin <token> first.");
            }
            onComplete.accept(false);
            return;
        }
        NightbreakContentManager.checkAccessAsync(ownerPlugin, slug, accessInfo -> {
            if (accessInfo == null || !accessInfo.hasAccess) {
                if (player != null && player.isOnline()) {
                    player.sendMessage("\u00a7c[Nightbreak] You don't have access to this content.");
                    if (accessInfo != null) {
                        NightbreakContentManager.showAccessLinks(player, accessInfo);
                    }
                }
                onComplete.accept(false);
                return;
            }
            NightbreakAccount.VersionInfo versionInfo = versionCache.get(slug);
            String fileName = versionInfo != null && versionInfo.fileName != null ? versionInfo.fileName : slug + ".zip";
            File destinationFile = new File(destinationFolder, fileName);
            if (player != null && player.isOnline()) {
                player.sendMessage("\u00a7a[Nightbreak] Starting download of " + slug + "...");
            }
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)ownerPlugin, () -> {
                long[] lastUpdate = new long[]{0L};
                boolean success = NightbreakAccount.getInstance().download(slug, destinationFile, null, (bytesDownloaded, totalBytes) -> {
                    if (player != null && player.isOnline() && System.currentTimeMillis() - lastUpdate[0] > 2000L) {
                        lastUpdate[0] = System.currentTimeMillis();
                        String progress = totalBytes > 0L ? String.format("%.1f%%", (double)bytesDownloaded * 100.0 / (double)totalBytes) : NightbreakContentManager.formatBytes(bytesDownloaded);
                        Bukkit.getScheduler().runTask((Plugin)ownerPlugin, () -> {
                            if (player.isOnline()) {
                                player.sendMessage("\u00a77[Nightbreak] Downloading... " + progress);
                            }
                        });
                    }
                });
                Bukkit.getScheduler().runTask((Plugin)ownerPlugin, () -> {
                    if (success) {
                        if (player != null && player.isOnline()) {
                            player.sendMessage("\u00a7a[Nightbreak] Download complete! File saved to imports folder.");
                        }
                    } else if (player != null && player.isOnline()) {
                        player.sendMessage("\u00a7c[Nightbreak] Download failed. Please try again later.");
                    }
                    onComplete.accept(success);
                });
            });
        });
    }

    public static void showAccessLinks(Player player, NightbreakAccount.AccessInfo accessInfo) {
        player.sendMessage("\u00a76----------------------------------------------------");
        player.sendMessage("\u00a7eYou can get access to this content through:");
        if (accessInfo.patreonLink != null && !accessInfo.patreonLink.isEmpty()) {
            player.sendMessage("\u00a76\u2022 Patreon: \u00a79" + accessInfo.patreonLink);
        }
        if (accessInfo.itchLink != null && !accessInfo.itchLink.isEmpty()) {
            player.sendMessage("\u00a76\u2022 itch.io: \u00a79" + accessInfo.itchLink);
        }
        if ((accessInfo.patreonLink == null || accessInfo.patreonLink.isEmpty()) && (accessInfo.itchLink == null || accessInfo.itchLink.isEmpty())) {
            player.sendMessage("\u00a76\u2022 Visit: \u00a79https://nightbreak.io");
        }
        player.sendMessage("\u00a76----------------------------------------------------");
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024L) {
            return bytes + " B";
        }
        if (bytes < 0x100000L) {
            return String.format("%.1f KB", (double)bytes / 1024.0);
        }
        return String.format("%.1f MB", (double)bytes / 1048576.0);
    }

    public static void shutdown() {
        NightbreakContentManager.clearCache();
    }

    @Generated
    public static Map<String, NightbreakAccount.AccessInfo> getAccessCache() {
        return accessCache;
    }

    @Generated
    public static Map<String, NightbreakAccount.VersionInfo> getVersionCache() {
        return versionCache;
    }
}

