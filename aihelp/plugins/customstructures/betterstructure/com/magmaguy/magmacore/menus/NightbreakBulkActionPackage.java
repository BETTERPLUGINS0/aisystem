/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.magmacore.menus;

import com.magmaguy.magmacore.menus.ContentPackage;
import com.magmaguy.magmacore.menus.NightbreakSetupVisuals;
import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.nightbreak.NightbreakManagedContent;
import com.magmaguy.magmacore.util.ChatColorConverter;
import com.magmaguy.magmacore.util.SpigotMessage;
import java.util.HashSet;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NightbreakBulkActionPackage<T extends NightbreakManagedContent>
extends ContentPackage {
    private final String pluginDisplayName;
    private final String contentPageUrl;
    private final String commandToRun;
    private final List<T> allPackages;
    private final ItemStack displayIcon;

    public NightbreakBulkActionPackage(String pluginDisplayName, String contentPageUrl, String commandToRun, List<T> allPackages) {
        this.pluginDisplayName = pluginDisplayName;
        this.contentPageUrl = contentPageUrl;
        this.commandToRun = commandToRun;
        this.allPackages = allPackages;
        this.displayIcon = this.buildIcon();
    }

    private ItemStack buildIcon() {
        List lore;
        String displayName;
        Material baseMaterial;
        String iconModel;
        if (!NightbreakAccount.hasToken()) {
            iconModel = "elitemobs:ui/redcross";
            baseMaterial = Material.RED_STAINED_GLASS_PANE;
            displayName = "&cDownload All";
            lore = List.of((Object)"&7No Nightbreak token linked.", (Object)"&7Click for setup instructions.");
        } else {
            long notDownloadedCount = this.countNotDownloaded();
            long outdatedCount = this.countOutdated();
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
        return NightbreakSetupVisuals.bulkActionItem(displayName, lore, baseMaterial, iconModel);
    }

    private long countNotDownloaded() {
        HashSet<String> seenSlugs = new HashSet<String>();
        long count = 0L;
        for (NightbreakManagedContent pkg : this.allPackages) {
            String slug = pkg.getNightbreakSlug();
            if (slug == null || slug.isEmpty() || seenSlugs.contains(slug) || pkg.isDownloaded() || pkg.getCachedAccessInfo() == null || !pkg.getCachedAccessInfo().hasAccess) continue;
            ++count;
            seenSlugs.add(slug);
        }
        return count;
    }

    private long countOutdated() {
        HashSet<String> seenSlugs = new HashSet<String>();
        long count = 0L;
        for (NightbreakManagedContent pkg : this.allPackages) {
            String slug;
            if (!pkg.isOutOfDate() || (slug = pkg.getNightbreakSlug()) == null || slug.isEmpty() || pkg.getCachedAccessInfo() != null && !pkg.getCachedAccessInfo().hasAccess || !seenSlugs.add(slug)) continue;
            ++count;
        }
        return count;
    }

    @Override
    public ItemStack getItemstack() {
        return this.displayIcon;
    }

    @Override
    public void onClick(Player player) {
        player.closeInventory();
        if (!NightbreakAccount.hasToken()) {
            player.sendMessage(ChatColorConverter.convert("&8&m----------------------------------------------------"));
            player.sendMessage(ChatColorConverter.convert("&eLink your Nightbreak account first to install " + this.pluginDisplayName + " content in-game."));
            player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&6Account page: "), SpigotMessage.hoverLinkMessage("&9&nhttps://nightbreak.io/account/", "&7Click to open Nightbreak account", "https://nightbreak.io/account/")});
            player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&6Content page: "), SpigotMessage.hoverLinkMessage("&9&n" + this.contentPageUrl, "&7Click to browse content", this.contentPageUrl)});
            player.sendMessage(ChatColorConverter.convert("&aUse &2/nightbreaklogin <token> &ato link your account, then click this button again."));
            player.sendMessage(ChatColorConverter.convert("&8&m----------------------------------------------------"));
            return;
        }
        boolean hasNotDownloaded = false;
        boolean hasOutdated = false;
        for (NightbreakManagedContent pkg : this.allPackages) {
            String slug = pkg.getNightbreakSlug();
            if (slug == null || slug.isEmpty()) continue;
            if (!pkg.isDownloaded() && pkg.getCachedAccessInfo() != null && pkg.getCachedAccessInfo().hasAccess) {
                hasNotDownloaded = true;
            }
            if (pkg.isOutOfDate()) {
                hasOutdated = true;
            }
            if (!hasNotDownloaded || !hasOutdated) continue;
            break;
        }
        if (!hasNotDownloaded && !hasOutdated) {
            player.sendMessage(ChatColorConverter.convert("&a[" + this.pluginDisplayName + "] No new content to download! All available packages are already downloaded."));
            return;
        }
        Bukkit.dispatchCommand((CommandSender)player, (String)this.commandToRun);
    }

    @Override
    protected ItemStack getInstalledItemStack() {
        return this.displayIcon;
    }

    @Override
    protected ItemStack getPartiallyInstalledItemStack() {
        return this.displayIcon;
    }

    @Override
    protected ItemStack getNotInstalledItemStack() {
        return this.displayIcon;
    }

    @Override
    protected ItemStack getNotDownloadedItemStack() {
        return this.displayIcon;
    }

    @Override
    protected ItemStack getNeedsAccessItemStack() {
        return this.displayIcon;
    }

    @Override
    protected ItemStack getOutOfDateUpdatableItemStack() {
        return this.displayIcon;
    }

    @Override
    protected ItemStack getOutOfDateNoAccessItemStack() {
        return this.displayIcon;
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

