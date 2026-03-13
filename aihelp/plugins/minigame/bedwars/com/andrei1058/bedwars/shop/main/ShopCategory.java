/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.shop.main;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.CategoryContent;
import com.andrei1058.bedwars.shop.main.ShopIndex;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopCategory {
    private int slot;
    private ItemStack itemStack;
    private String itemNamePath;
    private String itemLorePath;
    private String invNamePath;
    private boolean loaded = false;
    private final List<CategoryContent> categoryContentList = new ArrayList<CategoryContent>();
    public static List<UUID> categoryViewers = new ArrayList<UUID>();
    private final String name;

    public ShopCategory(String path, YamlConfiguration yml) {
        BedWars.debug("Loading shop category: " + path);
        this.name = path;
        if (yml.get(path + ".category-item.material") == null) {
            BedWars.plugin.getLogger().severe("Category material not set at: " + path);
            return;
        }
        if (yml.get(path + ".category-slot") == null) {
            BedWars.plugin.getLogger().severe("Category slot not set at: " + path);
            return;
        }
        this.slot = yml.getInt(path + ".category-slot");
        if (this.slot < 1 || this.slot > 8) {
            BedWars.plugin.getLogger().severe("Slot must be n > 1 and n < 9 at: " + path);
            return;
        }
        for (ShopCategory sc : ShopManager.shop.getCategoryList()) {
            if (sc.getSlot() != this.slot) continue;
            BedWars.plugin.getLogger().severe("Slot is already in use at: " + path);
            return;
        }
        this.itemStack = BedWars.nms.createItemStack(yml.getString(path + ".category-item.material"), yml.get(path + ".category-item.amount") == null ? 1 : yml.getInt(path + ".category-item.amount"), (short)(yml.get(path + ".category-item.data") == null ? 0 : yml.getInt(path + ".category-item.data")));
        if (yml.get(path + ".category-item.enchanted") != null && yml.getBoolean(path + ".category-item.enchanted")) {
            this.itemStack = ShopManager.enchantItem(this.itemStack);
        }
        if (yml.getString(path + ".category-item.potion-display") != null && !yml.getString(path + ".category-item.potion-display").isEmpty()) {
            this.itemStack = BedWars.nms.setTag(this.itemStack, "Potion", yml.getString(path + ".category-item.potion-display"));
        }
        if (yml.getString(path + ".category-item.potion-color") != null && !yml.getString(path + ".category-item.potion-color").isEmpty()) {
            this.itemStack = BedWars.nms.setTag(this.itemStack, "CustomPotionColor", yml.getString(path + ".category-item.potion-color"));
        }
        if (this.itemStack.getItemMeta() != null) {
            this.itemStack.setItemMeta(ShopManager.hideItemStuff(this.itemStack.getItemMeta()));
        }
        this.itemNamePath = "shop-items-messages.%category%.category-item-name".replace("%category%", path);
        this.itemLorePath = "shop-items-messages.%category%.category-item-lore".replace("%category%", path);
        this.invNamePath = "shop-items-messages.%category%.inventory-name".replace("%category%", path);
        this.loaded = true;
        for (String s : yml.getConfigurationSection(path + "..category-content").getKeys(false)) {
            CategoryContent cc = new CategoryContent(path + ".category-content." + s, s, path, yml, this);
            if (!cc.isLoaded()) continue;
            this.categoryContentList.add(cc);
            BedWars.debug("Adding CategoryContent: " + s + " to Shop Category: " + path);
        }
    }

    public void open(Player player, ShopIndex index, ShopCache shopCache) {
        if (player.getOpenInventory().getTopInventory() == null) {
            return;
        }
        ShopIndex.indexViewers.remove(player.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, (int)index.getInvSize(), (String)Language.getMsg(player, this.invNamePath));
        inv.setItem(index.getQuickBuyButton().getSlot(), index.getQuickBuyButton().getItemStack(player));
        for (ShopCategory sc : index.getCategoryList()) {
            inv.setItem(sc.getSlot(), sc.getItemStack(player));
        }
        index.addSeparator(player, inv);
        inv.setItem(this.getSlot() + 9, index.getSelectedItem(player));
        shopCache.setSelectedCategory(this.getSlot());
        for (CategoryContent cc : this.getCategoryContentList()) {
            inv.setItem(cc.getSlot(), cc.getItemStack(player, shopCache));
        }
        player.openInventory(inv);
        if (!categoryViewers.contains(player.getUniqueId())) {
            categoryViewers.add(player.getUniqueId());
        }
    }

    public ItemStack getItemStack(Player player) {
        ItemStack i = this.itemStack.clone();
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(Language.getMsg(player, this.itemNamePath));
            im.setLore(Language.getList(player, this.itemLorePath));
            i.setItemMeta(im);
        }
        return i;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public int getSlot() {
        return this.slot;
    }

    public List<CategoryContent> getCategoryContentList() {
        return this.categoryContentList;
    }

    public static CategoryContent getCategoryContent(String identifier, ShopIndex shopIndex) {
        for (ShopCategory sc : shopIndex.getCategoryList()) {
            for (CategoryContent cc : sc.getCategoryContentList()) {
                if (!cc.getIdentifier().equals(identifier)) continue;
                return cc;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public static List<UUID> getCategoryViewers() {
        return new ArrayList<UUID>(categoryViewers);
    }
}

