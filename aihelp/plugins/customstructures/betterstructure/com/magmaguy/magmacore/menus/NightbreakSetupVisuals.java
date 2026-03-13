/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.magmaguy.magmacore.menus;

import com.magmaguy.magmacore.menus.NightbreakSetupIcons;
import com.magmaguy.magmacore.nightbreak.NightbreakAccount;
import com.magmaguy.magmacore.util.ItemStackGenerator;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class NightbreakSetupVisuals {
    private NightbreakSetupVisuals() {
    }

    public static ItemStack installedItem(String name, List<String> description) {
        return NightbreakSetupVisuals.stateItem(name, description, List.of((Object)"Content is installed!", (Object)"Click to uninstall!"), Material.GREEN_STAINED_GLASS_PANE, "elitemobs:ui/checkmark");
    }

    public static ItemStack partiallyInstalledItem(String name, List<String> description) {
        return NightbreakSetupVisuals.stateItem(name, description, List.of((Object)"Content is partially installed!", (Object)"Some files are missing or disabled.", (Object)"Click to download the latest content again.", (Object)"This will restore the package to a clean state."), Material.ORANGE_STAINED_GLASS_PANE, "elitemobs:ui/gray_x");
    }

    public static ItemStack notInstalledItem(String name, List<String> description) {
        return NightbreakSetupVisuals.stateItem(name, description, List.of((Object)"Content is downloaded but disabled.", (Object)"Click to install!"), Material.YELLOW_STAINED_GLASS_PANE, "elitemobs:ui/gray_x");
    }

    public static ItemStack notDownloadedItem(String name, List<String> description, String nightbreakSlug, NightbreakAccount.AccessInfo cachedAccessInfo) {
        String modelId = nightbreakSlug == null || nightbreakSlug.isEmpty() ? "elitemobs:ui/unlocked" : (!NightbreakAccount.hasToken() ? "elitemobs:ui/lockedunlinked" : (cachedAccessInfo != null && cachedAccessInfo.hasAccess ? "elitemobs:ui/unlocked" : "elitemobs:ui/lockedunpaid"));
        return NightbreakSetupVisuals.stateItem(name, description, List.of((Object)"Content is not downloaded yet.", (Object)"Click to download it!"), Material.YELLOW_STAINED_GLASS_PANE, modelId);
    }

    public static ItemStack needsAccessItem(String name, List<String> description, NightbreakAccount.AccessInfo cachedAccessInfo) {
        ArrayList<String> tooltip = new ArrayList<String>();
        tooltip.add("You need Nightbreak access for this content.");
        tooltip.add("Click to see access links.");
        if (cachedAccessInfo != null) {
            if (cachedAccessInfo.patreonLink != null && !cachedAccessInfo.patreonLink.isEmpty()) {
                tooltip.add("Available on Patreon.");
            }
            if (cachedAccessInfo.itchLink != null && !cachedAccessInfo.itchLink.isEmpty()) {
                tooltip.add("Available on itch.io.");
            }
        }
        return NightbreakSetupVisuals.stateItem(name, description, tooltip, Material.PURPLE_STAINED_GLASS_PANE, "elitemobs:ui/lockedunpaid");
    }

    public static ItemStack outOfDateUpdatableItem(String name, List<String> description, String nightbreakSlug) {
        String modelId = nightbreakSlug == null || nightbreakSlug.isEmpty() ? "elitemobs:ui/update" : (!NightbreakAccount.hasToken() ? "elitemobs:ui/updateunlinked" : "elitemobs:ui/update");
        return NightbreakSetupVisuals.stateItem(name, description, List.of((Object)"An update is available!", (Object)"Click to download the update."), Material.YELLOW_STAINED_GLASS_PANE, modelId);
    }

    public static ItemStack outOfDateNoAccessItem(String name, List<String> description) {
        return NightbreakSetupVisuals.stateItem(name, description, List.of((Object)"An update is available!", (Object)"You need Nightbreak access before you can update it.", (Object)"Click to see access links."), Material.ORANGE_STAINED_GLASS_PANE, "elitemobs:ui/updateunpaid");
    }

    public static ItemStack bulkActionItem(String name, List<String> lore, Material material, String modelId) {
        return NightbreakSetupVisuals.stateItem(name, List.of(), lore, material, modelId);
    }

    public static ItemStack stateItem(String name, List<String> description, List<String> specificTooltip, Material material, String modelId) {
        ArrayList<String> tooltip = new ArrayList<String>(specificTooltip);
        tooltip.addAll(description);
        ItemStack itemStack = ItemStackGenerator.generateItemStack(material, name, tooltip);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP});
            itemStack.setItemMeta(itemMeta);
        }
        NightbreakSetupIcons.applyItemModel(itemStack, modelId);
        return itemStack;
    }
}

