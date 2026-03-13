/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.MetadataHandler;
import com.magmaguy.betterstructures.commands.ReloadCommand;
import com.magmaguy.betterstructures.content.BSPackage;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.nightbreak.NightbreakContentManager;
import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DownloadAllContentCommand
extends AdvancedCommand {
    static final AtomicBoolean IS_BULK_DOWNLOADING = new AtomicBoolean(false);

    public DownloadAllContentCommand() {
        super(List.of((Object)"downloadall"));
        this.setPermission("betterstructures.setup");
        this.setSenderType(SenderType.ANY);
        this.setDescription("Downloads all BetterStructures content available through Nightbreak.");
        this.setUsage("/bs downloadall");
    }

    @Override
    public void execute(CommandData commandData) {
        DownloadAllContentCommand.execute(commandData.getCommandSender(), false);
    }

    public static void execute(CommandSender sender, boolean updatesOnly) {
        Player playerSender;
        if (!NightbreakAccount.hasToken()) {
            Logger.sendSimpleMessage(sender, "&cLink your Nightbreak account first with &a/nightbreaklogin <token>&c.");
            return;
        }
        if (!IS_BULK_DOWNLOADING.compareAndSet(false, true)) {
            Logger.sendSimpleMessage(sender, "&eA BetterStructures bulk content operation is already running.");
            return;
        }
        List<BSPackage> packagesToDownload = DownloadAllContentCommand.collectPackages(updatesOnly);
        if (packagesToDownload.isEmpty()) {
            IS_BULK_DOWNLOADING.set(false);
            Logger.sendSimpleMessage(sender, updatesOnly ? "&aAll BetterStructures content is already up to date." : "&aAll BetterStructures Nightbreak content is already downloaded and up to date.");
            return;
        }
        Logger.sendSimpleMessage(sender, updatesOnly ? "&aFound &2" + packagesToDownload.size() + "&a BetterStructures package update(s)." : "&aFound &2" + packagesToDownload.size() + "&a BetterStructures content package(s) to download or update.");
        Player player = sender instanceof Player ? (playerSender = (Player)sender) : null;
        File importsFolder = new File(MetadataHandler.PLUGIN.getDataFolder(), "imports");
        if (!importsFolder.exists()) {
            importsFolder.mkdirs();
        }
        DownloadAllContentCommand.downloadNext((JavaPlugin)MetadataHandler.PLUGIN, packagesToDownload, 0, importsFolder, sender, player, new AtomicInteger(), new AtomicInteger(), new ArrayList<String>(), updatesOnly);
    }

    static List<BSPackage> collectPackages(boolean updatesOnly) {
        ArrayList<BSPackage> packagesToDownload = new ArrayList<BSPackage>();
        HashSet<String> seenSlugs = new HashSet<String>();
        for (BSPackage bsPackage : BSPackage.getBsPackages().values()) {
            String slug = bsPackage.getNightbreakSlug();
            if (slug == null || slug.isEmpty() || !seenSlugs.add(slug)) continue;
            if (updatesOnly) {
                if (!bsPackage.isOutOfDate() || bsPackage.getCachedAccessInfo() != null && !bsPackage.getCachedAccessInfo().hasAccess) continue;
                packagesToDownload.add(bsPackage);
                continue;
            }
            if (bsPackage.isDownloaded() && !bsPackage.isOutOfDate() || bsPackage.getCachedAccessInfo() != null && !bsPackage.getCachedAccessInfo().hasAccess) continue;
            packagesToDownload.add(bsPackage);
        }
        return packagesToDownload;
    }

    private static void downloadNext(JavaPlugin plugin, List<BSPackage> packages, int index, File importsFolder, CommandSender sender, Player player, AtomicInteger completed, AtomicInteger failed, List<String> failedNames, boolean updatesOnly) {
        if (player != null && !player.isOnline()) {
            if (index >= packages.size()) {
                IS_BULK_DOWNLOADING.set(false);
                if (completed.get() > 0) {
                    Bukkit.getScheduler().runTaskLater((Plugin)plugin, () -> ReloadCommand.reload((CommandSender)Bukkit.getConsoleSender()), 20L);
                }
                return;
            }
            BSPackage bsPackage = packages.get(index);
            NightbreakContentManager.downloadAsync(plugin, bsPackage.getNightbreakSlug(), importsFolder, null, success -> {
                if (success.booleanValue()) {
                    completed.incrementAndGet();
                } else {
                    failed.incrementAndGet();
                    failedNames.add(bsPackage.getDisplayName());
                }
                DownloadAllContentCommand.downloadNext(plugin, packages, index + 1, importsFolder, sender, player, completed, failed, failedNames, updatesOnly);
            });
            return;
        }
        if (index >= packages.size()) {
            IS_BULK_DOWNLOADING.set(false);
            Logger.sendSimpleMessage(sender, updatesOnly ? "&aBetterStructures updates finished. Updated &2" + completed.get() + "&a, failed &c" + failed.get() + "&a." : "&aBetterStructures bulk download finished. Downloaded &2" + completed.get() + "&a, failed &c" + failed.get() + "&a.");
            if (!failedNames.isEmpty()) {
                Logger.sendSimpleMessage(sender, "&cFailed packages: " + String.join((CharSequence)", ", failedNames));
            }
            if (completed.get() > 0) {
                Logger.sendSimpleMessage(sender, "&aReloading BetterStructures to pick up the new content...");
                Bukkit.getScheduler().runTaskLater((Plugin)plugin, () -> ReloadCommand.reload(sender), 20L);
            }
            return;
        }
        BSPackage bsPackage = packages.get(index);
        Logger.sendSimpleMessage(sender, updatesOnly ? "&2Updating (&a" + (index + 1) + "&2/&a" + packages.size() + "&2) &a" + bsPackage.getDisplayName() + "&2..." : "&2Downloading (&a" + (index + 1) + "&2/&a" + packages.size() + "&2) &a" + bsPackage.getDisplayName() + "&2...");
        NightbreakContentManager.downloadAsync(plugin, bsPackage.getNightbreakSlug(), importsFolder, null, success -> {
            if (success.booleanValue()) {
                completed.incrementAndGet();
                if (player == null || player.isOnline()) {
                    int remaining = packages.size() - (index + 1);
                    Logger.sendSimpleMessage(sender, remaining > 0 ? "&aDownloaded " + bsPackage.getDisplayName() + "&a. &2" + remaining + "&a package(s) remaining..." : "&aDownloaded " + bsPackage.getDisplayName() + "&a.");
                }
            } else {
                failed.incrementAndGet();
                failedNames.add(bsPackage.getDisplayName());
                if (player == null || player.isOnline()) {
                    Logger.sendSimpleMessage(sender, "&cFailed to download " + bsPackage.getDisplayName() + "&c.");
                }
            }
            DownloadAllContentCommand.downloadNext(plugin, packages, index + 1, importsFolder, sender, player, completed, failed, failedNames, updatesOnly);
        });
    }
}

