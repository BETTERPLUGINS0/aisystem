/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.shop.main;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.shop.IBuyItem;
import com.andrei1058.bedwars.api.arena.shop.IContentTier;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.BuyCommand;
import com.andrei1058.bedwars.shop.main.BuyItem;
import com.andrei1058.bedwars.shop.main.CategoryContent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ContentTier
implements IContentTier {
    private int value;
    private int price;
    private ItemStack itemStack;
    private Material currency;
    private List<IBuyItem> buyItemsList = new ArrayList<IBuyItem>();
    private boolean loaded = false;

    public ContentTier(String path, String tierName, String identifier, YamlConfiguration yml) {
        IBuyItem bi;
        BedWars.debug("Loading content tier" + path);
        if (yml.get(path + ".tier-item.material") == null) {
            BedWars.plugin.getLogger().severe("tier-item material not set at " + path);
            return;
        }
        try {
            this.value = Integer.parseInt(tierName.replace("tier", ""));
        } catch (Exception e) {
            BedWars.plugin.getLogger().severe(path + " doesn't end with a number. It's not recognized as a tier!");
            return;
        }
        if (yml.get(path + ".tier-settings.cost") == null) {
            BedWars.plugin.getLogger().severe("Cost not set for " + path);
            return;
        }
        this.price = yml.getInt(path + ".tier-settings.cost");
        if (yml.get(path + ".tier-settings.currency") == null) {
            BedWars.plugin.getLogger().severe("Currency not set for " + path);
            return;
        }
        if (yml.getString(path + ".tier-settings.currency").isEmpty()) {
            BedWars.plugin.getLogger().severe("Invalid currency at " + path);
            return;
        }
        switch (yml.getString(path + ".tier-settings.currency").toLowerCase()) {
            case "iron": 
            case "gold": 
            case "diamond": 
            case "vault": 
            case "emerald": {
                this.currency = CategoryContent.getCurrency(yml.getString(path + ".tier-settings.currency").toLowerCase());
                break;
            }
            default: {
                BedWars.plugin.getLogger().severe("Invalid currency at " + path);
                this.currency = Material.IRON_INGOT;
            }
        }
        this.itemStack = BedWars.nms.createItemStack(yml.getString(path + ".tier-item.material"), yml.get(path + ".tier-item.amount") == null ? 1 : yml.getInt(path + ".tier-item.amount"), (short)(yml.get(path + ".tier-item.data") == null ? 0 : yml.getInt(path + ".tier-item.data")));
        if (yml.get(path + ".tier-item.enchanted") != null && yml.getBoolean(path + ".tier-item.enchanted")) {
            this.itemStack = ShopManager.enchantItem(this.itemStack);
        }
        if (yml.getString(path + ".tier-item.potion-display") != null && !yml.getString(path + ".tier-item.potion-display").isEmpty()) {
            this.itemStack = BedWars.nms.setTag(this.itemStack, "Potion", yml.getString(path + ".tier-item.potion-display"));
        }
        if (yml.getString(path + ".tier-item.potion-color") != null && !yml.getString(path + ".tier-item.potion-color").isEmpty()) {
            this.itemStack = BedWars.nms.setTag(this.itemStack, "CustomPotionColor", yml.getString(path + ".tier-item.potion-color"));
        }
        if (this.itemStack != null) {
            this.itemStack.setItemMeta(ShopManager.hideItemStuff(this.itemStack.getItemMeta()));
        }
        if (yml.get(path + ".buy-items") != null) {
            for (String s : yml.getConfigurationSection(path + ".buy-items").getKeys(false)) {
                bi = new BuyItem(path + ".buy-items." + s, yml, identifier, this);
                if (!bi.isLoaded()) continue;
                this.buyItemsList.add(bi);
            }
        }
        if (yml.get(path + ".buy-cmds") != null && (bi = new BuyCommand(path + ".buy-cmds", yml, identifier)).isLoaded()) {
            this.buyItemsList.add(bi);
        }
        if (this.buyItemsList.isEmpty()) {
            Bukkit.getLogger().warning("Loaded 0 buy content for: " + path);
        }
        this.loaded = true;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public Material getCurrency() {
        return this.currency;
    }

    @Override
    public void setCurrency(Material currency) {
        this.currency = currency;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void setBuyItemsList(List<IBuyItem> buyItemsList) {
        this.buyItemsList = buyItemsList;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public List<IBuyItem> getBuyItemsList() {
        return this.buyItemsList;
    }
}

