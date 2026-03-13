/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.magmaguy.magmacore.nightbreak;

import com.magmaguy.magmacore.menus.SetupMenuIcons;
import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.util.ItemStackGenerator;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.SpigotMessage;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class NightbreakSetupMenuHelper {
    private static final String SEPARATOR = "<g:#8B0000:#CC4400:#DAA520>\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac</g>";

    private NightbreakSetupMenuHelper() {
    }

    public static ItemStack createItem(String displayName, List<String> description, Material material, String modelId, List<String> stateLore) {
        ArrayList<String> lore = new ArrayList<String>(stateLore);
        lore.addAll(description);
        ItemStack itemStack = ItemStackGenerator.generateItemStack(material, displayName, lore);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP});
            itemStack.setItemMeta(itemMeta);
        }
        SetupMenuIcons.applyItemModel(itemStack, modelId);
        return itemStack;
    }

    public static ItemStack createInstalledItem(String displayName, List<String> description) {
        return NightbreakSetupMenuHelper.createItem(displayName, description, Material.GREEN_STAINED_GLASS_PANE, "elitemobs:ui/checkmark", List.of((Object)"&aContent is installed!", (Object)"&7Click to uninstall it."));
    }

    public static ItemStack createPartiallyInstalledItem(String displayName, List<String> description) {
        return NightbreakSetupMenuHelper.createItem(displayName, description, Material.ORANGE_STAINED_GLASS_PANE, "elitemobs:ui/gray_x", List.of((Object)"&6Content is only partially installed!", (Object)"&7Some files appear to be missing or disabled.", (Object)"&7Click to redownload and repair this package."));
    }

    public static ItemStack createNotInstalledItem(String displayName, List<String> description) {
        return NightbreakSetupMenuHelper.createItem(displayName, description, Material.YELLOW_STAINED_GLASS_PANE, "elitemobs:ui/gray_x", List.of((Object)"&eContent is downloaded but disabled.", (Object)"&7Click to install it."));
    }

    public static ItemStack createNotDownloadedItem(String displayName, List<String> description, String slug, NightbreakAccount.AccessInfo accessInfo) {
        String modelId = slug == null || slug.isEmpty() ? "elitemobs:ui/unlocked" : (!NightbreakAccount.hasToken() ? "elitemobs:ui/lockedunlinked" : (accessInfo != null && accessInfo.hasAccess ? "elitemobs:ui/unlocked" : "elitemobs:ui/lockedunpaid"));
        return NightbreakSetupMenuHelper.createItem(displayName, description, Material.YELLOW_STAINED_GLASS_PANE, modelId, List.of((Object)"&eContent is not downloaded yet.", (Object)"&7Click to download it."));
    }

    public static ItemStack createNeedsAccessItem(String displayName, List<String> description, NightbreakAccount.AccessInfo accessInfo) {
        ArrayList<String> stateLore = new ArrayList<String>();
        stateLore.add("&dNightbreak access is required.");
        stateLore.add("&7Click to view access links.");
        if (accessInfo != null) {
            if (accessInfo.patreonLink != null && !accessInfo.patreonLink.isEmpty()) {
                stateLore.add("&5Available on Patreon");
            }
            if (accessInfo.itchLink != null && !accessInfo.itchLink.isEmpty()) {
                stateLore.add("&5Available on itch.io");
            }
        }
        return NightbreakSetupMenuHelper.createItem(displayName, description, Material.PURPLE_STAINED_GLASS_PANE, "elitemobs:ui/lockedunpaid", stateLore);
    }

    public static ItemStack createOutOfDateUpdatableItem(String displayName, List<String> description, String slug) {
        String modelId = slug == null || slug.isEmpty() ? "elitemobs:ui/update" : (!NightbreakAccount.hasToken() ? "elitemobs:ui/updateunlinked" : "elitemobs:ui/update");
        return NightbreakSetupMenuHelper.createItem(displayName, description, Material.YELLOW_STAINED_GLASS_PANE, modelId, List.of((Object)"&eAn update is available!", (Object)"&7Click to download the update."));
    }

    public static ItemStack createOutOfDateNoAccessItem(String displayName, List<String> description) {
        return NightbreakSetupMenuHelper.createItem(displayName, description, Material.ORANGE_STAINED_GLASS_PANE, "elitemobs:ui/updateunpaid", List.of((Object)"&6An update is available!", (Object)"&7You need Nightbreak access before you can update it.", (Object)"&7Click to view access links."));
    }

    public static void sendNoTokenPrompt(Player player, String pluginName, String contentUrl) {
        Logger.sendSimpleMessage((CommandSender)player, SEPARATOR);
        Logger.sendSimpleMessage((CommandSender)player, "&6Link your Nightbreak account to manage " + pluginName + " content in-game.");
        player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&2Account page: "), SpigotMessage.hoverLinkMessage("&ahttps://nightbreak.io/account/", "&7Click to open your Nightbreak account page.", "https://nightbreak.io/account/")});
        Logger.sendSimpleMessage((CommandSender)player, "&2Link command: &a/nightbreaklogin <token>");
        player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&2" + pluginName + " content: "), SpigotMessage.hoverLinkMessage("&a" + contentUrl, "&7Click to browse Nightbreak content for " + pluginName + ".", contentUrl)});
        Logger.sendSimpleMessage((CommandSender)player, SEPARATOR);
    }

    public static void sendManualDownloadMessage(CommandSender sender, String downloadLink, String importsFolderName, String reloadCommand) {
        Logger.sendSimpleMessage(sender, SEPARATOR);
        if (sender instanceof Player) {
            Player player = (Player)sender;
            player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&6Download this package here: "), SpigotMessage.hoverLinkMessage("&9&n" + downloadLink, "&7Click to open the download link.", downloadLink)});
        } else {
            Logger.sendSimpleMessage(sender, "&6Download this package here: &9&n" + downloadLink);
        }
        Logger.sendSimpleMessage(sender, "&7Then put the downloaded file in the &f" + importsFolderName + "&7 folder.");
        if (reloadCommand != null && !reloadCommand.isEmpty()) {
            Logger.sendSimpleMessage(sender, "&7Finish by running &a" + reloadCommand + "&7.");
        }
        Logger.sendSimpleMessage(sender, SEPARATOR);
    }

    public static void sendAccessInfo(Player player, String packageName, NightbreakAccount.AccessInfo accessInfo, String contentUrl) {
        Logger.sendSimpleMessage((CommandSender)player, SEPARATOR);
        Logger.sendSimpleMessage((CommandSender)player, "&6You do not currently have access to " + packageName + "&6.");
        player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&2Browse the content page: "), SpigotMessage.hoverLinkMessage("&a" + contentUrl, "&7Click to open the Nightbreak content page.", contentUrl)});
        if (accessInfo != null) {
            if (accessInfo.patreonLink != null && !accessInfo.patreonLink.isEmpty()) {
                player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&2Patreon: "), SpigotMessage.hoverLinkMessage("&a" + accessInfo.patreonLink, "&7Click to open Patreon.", accessInfo.patreonLink)});
            }
            if (accessInfo.itchLink != null && !accessInfo.itchLink.isEmpty()) {
                player.spigot().sendMessage(new BaseComponent[]{SpigotMessage.simpleMessage("&2itch.io: "), SpigotMessage.hoverLinkMessage("&a" + accessInfo.itchLink, "&7Click to open itch.io.", accessInfo.itchLink)});
            }
        }
        Logger.sendSimpleMessage((CommandSender)player, "&2Account link command: &a/nightbreaklogin <token>");
        Logger.sendSimpleMessage((CommandSender)player, SEPARATOR);
    }

    public static String getSeparator() {
        return SEPARATOR;
    }
}

