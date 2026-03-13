/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.shop.main;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.events.shop.ShopOpenEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.main.QuickBuyButton;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import com.andrei1058.bedwars.shop.quickbuy.PlayerQuickBuyCache;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopIndex {
    private int invSize = 54;
    private String namePath;
    private String separatorNamePath;
    private String separatorLorePath;
    private List<ShopCategory> categoryList = new ArrayList<ShopCategory>();
    private QuickBuyButton quickBuyButton;
    public ItemStack separatorSelected;
    public ItemStack separatorStandard;
    public static List<UUID> indexViewers = new ArrayList<UUID>();

    public ShopIndex(String namePath, QuickBuyButton quickBuyButton, String separatorNamePath, String separatorLorePath, ItemStack separatorSelected, ItemStack separatorStandard) {
        this.namePath = namePath;
        this.separatorLorePath = separatorLorePath;
        this.separatorNamePath = separatorNamePath;
        this.quickBuyButton = quickBuyButton;
        this.separatorStandard = separatorStandard;
        this.separatorSelected = separatorSelected;
    }

    public void open(Player player, PlayerQuickBuyCache quickBuyCache, boolean callEvent) {
        if (quickBuyCache == null) {
            return;
        }
        if (callEvent) {
            ShopOpenEvent event = new ShopOpenEvent(player, Arena.getArenaByPlayer(player));
            Bukkit.getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                return;
            }
        }
        Inventory inv = Bukkit.createInventory(null, (int)this.invSize, (String)Language.getMsg(player, this.getNamePath()));
        inv.setItem(this.getQuickBuyButton().getSlot(), this.getQuickBuyButton().getItemStack(player));
        for (ShopCategory sc : this.getCategoryList()) {
            inv.setItem(sc.getSlot(), sc.getItemStack(player));
        }
        this.addSeparator(player, inv);
        inv.setItem(this.getQuickBuyButton().getSlot() + 9, this.getSelectedItem(player));
        ShopCache.getShopCache(player.getUniqueId()).setSelectedCategory(this.getQuickBuyButton().getSlot());
        quickBuyCache.addInInventory(inv, ShopCache.getShopCache(player.getUniqueId()));
        player.openInventory(inv);
        if (!indexViewers.contains(player.getUniqueId())) {
            indexViewers.add(player.getUniqueId());
        }
    }

    public void addSeparator(Player player, Inventory inv) {
        ItemStack i = this.separatorStandard.clone();
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(Language.getMsg(player, this.separatorNamePath));
            im.setLore(Language.getList(player, this.separatorLorePath));
            i.setItemMeta(im);
        }
        for (int x = 9; x < 18; ++x) {
            inv.setItem(x, i);
        }
    }

    public ItemStack getSelectedItem(Player player) {
        ItemStack i = this.separatorSelected.clone();
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(Language.getMsg(player, this.separatorNamePath));
            im.setLore(Language.getList(player, this.separatorLorePath));
            i.setItemMeta(im);
        }
        return i;
    }

    public void addShopCategory(ShopCategory sc) {
        this.categoryList.add(sc);
        BedWars.debug("Adding shop category: " + String.valueOf(sc) + " at slot " + sc.getSlot());
    }

    public String getNamePath() {
        return this.namePath;
    }

    public int getInvSize() {
        return this.invSize;
    }

    public List<ShopCategory> getCategoryList() {
        return this.categoryList;
    }

    public QuickBuyButton getQuickBuyButton() {
        return this.quickBuyButton;
    }

    public static List<UUID> getIndexViewers() {
        return new ArrayList<UUID>(indexViewers);
    }
}

