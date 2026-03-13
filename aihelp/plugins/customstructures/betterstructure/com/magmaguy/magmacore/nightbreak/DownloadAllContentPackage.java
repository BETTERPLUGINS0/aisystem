/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.magmacore.nightbreak;

import com.magmaguy.magmacore.menus.ContentPackage;
import com.magmaguy.magmacore.menus.SetupMenuIcons;
import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.nightbreak.NightbreakManagedContent;
import com.magmaguy.magmacore.nightbreak.NightbreakSetupMenuHelper;
import com.magmaguy.magmacore.util.ItemStackGenerator;
import com.magmaguy.magmacore.util.Logger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DownloadAllContentPackage<T extends NightbreakManagedContent>
extends ContentPackage {
    private final Supplier<Collection<T>> packagesSupplier;
    private final String pluginName;
    private final String contentUrl;
    private final String downloadAllCommand;

    public DownloadAllContentPackage(Supplier<Collection<T>> packagesSupplier, String pluginName, String contentUrl, String downloadAllCommand) {
        this.packagesSupplier = packagesSupplier;
        this.pluginName = pluginName;
        this.contentUrl = contentUrl;
        this.downloadAllCommand = downloadAllCommand;
    }

    @Override
    public ItemStack getItemstack() {
        List lore;
        String displayName;
        Material baseMaterial;
        String iconModel;
        Collection<T> allPackages = this.packagesSupplier.get();
        if (!NightbreakAccount.hasToken()) {
            iconModel = "elitemobs:ui/redcross";
            baseMaterial = Material.RED_STAINED_GLASS_PANE;
            displayName = "&cDownload All";
            lore = List.of((Object)"&7No Nightbreak token linked.", (Object)"&7Click for setup instructions.");
        } else {
            long notDownloadedCount = this.countNotDownloaded(allPackages);
            long outdatedCount = this.countOutdated(allPackages);
            if (notDownloadedCount > 0L && outdatedCount > 0L) {
                iconModel = "elitemobs:ui/yellowcrown";
                baseMaterial = Material.YELLOW_STAINED_GLASS_PANE;
                displayName = "&eDownload & Update All";
                lore = List.of((Object)("&7Click to download &e" + notDownloadedCount + " &7new package" + (notDownloadedCount != 1L ? "s" : "")), (Object)("&7and update &e" + outdatedCount + " &7outdated package" + (outdatedCount != 1L ? "s" : "") + "."));
            } else if (notDownloadedCount > 0L) {
                iconModel = "elitemobs:ui/yellowcrown";
                baseMaterial = Material.YELLOW_STAINED_GLASS_PANE;
                displayName = "&eDownload All";
                lore = List.of((Object)("&7Click to download &e" + notDownloadedCount + " &7available package" + (notDownloadedCount != 1L ? "s" : "") + "."));
            } else if (outdatedCount > 0L) {
                iconModel = "elitemobs:ui/yellowcrown";
                baseMaterial = Material.YELLOW_STAINED_GLASS_PANE;
                displayName = "&eUpdate All";
                lore = List.of((Object)("&7Click to update &e" + outdatedCount + " &7outdated package" + (outdatedCount != 1L ? "s" : "") + "."));
            } else {
                iconModel = "elitemobs:ui/checkmark";
                baseMaterial = Material.LIME_STAINED_GLASS_PANE;
                displayName = "&aAll Content Up To Date";
                lore = List.of((Object)"&7All available content is downloaded", (Object)"&7and up to date!");
            }
        }
        ItemStack itemStack = ItemStackGenerator.generateItemStack(baseMaterial, displayName, (List<String>)lore);
        SetupMenuIcons.applyItemModel(itemStack, iconModel);
        return itemStack;
    }

    private long countNotDownloaded(Collection<T> allPackages) {
        HashSet<String> seenSlugs = new HashSet<String>();
        long count = 0L;
        for (NightbreakManagedContent contentPackage : allPackages) {
            String slug = contentPackage.getNightbreakSlug();
            if (slug == null || slug.isEmpty() || !seenSlugs.add(slug) || contentPackage.isDownloaded() || contentPackage.getCachedAccessInfo() == null || !contentPackage.getCachedAccessInfo().hasAccess) continue;
            ++count;
        }
        return count;
    }

    private long countOutdated(Collection<T> allPackages) {
        HashSet<String> seenSlugs = new HashSet<String>();
        long count = 0L;
        for (NightbreakManagedContent contentPackage : allPackages) {
            String slug;
            if (!contentPackage.isOutOfDate() || (slug = contentPackage.getNightbreakSlug()) == null || slug.isEmpty() || contentPackage.getCachedAccessInfo() != null && !contentPackage.getCachedAccessInfo().hasAccess || !seenSlugs.add(slug)) continue;
            ++count;
        }
        return count;
    }

    @Override
    public void onClick(Player player) {
        player.closeInventory();
        if (!NightbreakAccount.hasToken()) {
            NightbreakSetupMenuHelper.sendNoTokenPrompt(player, this.pluginName, this.contentUrl);
            return;
        }
        boolean hasNotDownloaded = false;
        boolean hasOutdated = false;
        for (NightbreakManagedContent contentPackage : this.packagesSupplier.get()) {
            String slug = contentPackage.getNightbreakSlug();
            if (slug == null || slug.isEmpty()) continue;
            if (!contentPackage.isDownloaded() && contentPackage.getCachedAccessInfo() != null && contentPackage.getCachedAccessInfo().hasAccess) {
                hasNotDownloaded = true;
            }
            if (contentPackage.isOutOfDate()) {
                hasOutdated = true;
            }
            if (!hasNotDownloaded || !hasOutdated) continue;
            break;
        }
        if (!hasNotDownloaded && !hasOutdated) {
            Logger.sendSimpleMessage((CommandSender)player, "&aAll available " + this.pluginName + " content is already downloaded and up to date.");
            return;
        }
        Bukkit.dispatchCommand((CommandSender)player, (String)this.downloadAllCommand);
    }

    @Override
    protected ItemStack getInstalledItemStack() {
        return this.getItemstack();
    }

    @Override
    protected ItemStack getPartiallyInstalledItemStack() {
        return this.getItemstack();
    }

    @Override
    protected ItemStack getNotInstalledItemStack() {
        return this.getItemstack();
    }

    @Override
    protected ItemStack getNotDownloadedItemStack() {
        return this.getItemstack();
    }

    @Override
    protected ItemStack getNeedsAccessItemStack() {
        return this.getItemstack();
    }

    @Override
    protected ItemStack getOutOfDateUpdatableItemStack() {
        return this.getItemstack();
    }

    @Override
    protected ItemStack getOutOfDateNoAccessItemStack() {
        return this.getItemstack();
    }

    @Override
    protected void doInstall(Player player) {
    }

    @Override
    protected void doUninstall(Player player) {
    }

    @Override
    protected void doDownload(Player player) {
    }

    @Override
    protected void doShowAccessInfo(Player player) {
    }

    @Override
    protected ContentPackage.ContentState getContentState() {
        return ContentPackage.ContentState.INSTALLED;
    }
}

