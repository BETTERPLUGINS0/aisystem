/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.shop;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.CategoryContent;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopCache {
    private UUID player;
    private List<CachedItem> cachedItems = new LinkedList<CachedItem>();
    private int selectedCategory;
    private HashMap<ShopCategory, Byte> categoryWeight = new HashMap();
    private static List<ShopCache> shopCaches = new ArrayList<ShopCache>();

    public ShopCache(UUID player) {
        this.player = player;
        this.selectedCategory = ShopManager.getShop().getQuickBuyButton().getSlot();
        shopCaches.add(this);
    }

    public UUID getPlayer() {
        return this.player;
    }

    public void setSelectedCategory(int slot) {
        this.selectedCategory = slot;
    }

    public int getSelectedCategory() {
        return this.selectedCategory;
    }

    public int getContentTier(String identifier) {
        CachedItem ci = this.getCachedItem(identifier);
        return ci == null ? 1 : ci.getTier();
    }

    public static ShopCache getShopCache(UUID player) {
        for (ShopCache sc : new ArrayList<ShopCache>(shopCaches)) {
            if (!sc.player.equals(player)) continue;
            return sc;
        }
        return null;
    }

    public void destroy() {
        shopCaches.remove(this);
        this.cachedItems.clear();
        this.cachedItems = null;
        this.categoryWeight = null;
    }

    public void managePermanentsAndDowngradables(Arena arena) {
        BedWars.debug("Restore permanents on death for: " + String.valueOf(this.player));
        for (CachedItem ci : this.cachedItems) {
            ci.manageDeath(arena);
        }
    }

    public CachedItem getCachedItem(String identifier) {
        for (CachedItem ci : this.cachedItems) {
            if (!ci.getCc().getIdentifier().equals(identifier)) continue;
            return ci;
        }
        return null;
    }

    public boolean hasCachedItem(CategoryContent cc) {
        for (CachedItem ci : this.cachedItems) {
            if (ci.getCc() != cc) continue;
            return true;
        }
        return false;
    }

    public CachedItem getCachedItem(CategoryContent cc) {
        for (CachedItem ci : this.cachedItems) {
            if (ci.getCc() != cc) continue;
            return ci;
        }
        return null;
    }

    public void upgradeCachedItem(CategoryContent cc, int slot) {
        CachedItem ci = this.getCachedItem(cc.getIdentifier());
        if (ci == null) {
            ci = new CachedItem(cc);
            ci.updateItem(slot, Bukkit.getPlayer((UUID)this.player));
        } else if (cc.getContentTiers().size() > ci.getTier()) {
            BedWars.debug("Cached item upgrade for " + cc.getIdentifier() + " player " + String.valueOf(this.player));
            ci.upgrade(slot);
        }
    }

    public void setCategoryWeight(ShopCategory sc, byte weight) {
        if (this.categoryWeight.containsKey(sc)) {
            this.categoryWeight.replace(sc, weight);
        } else {
            this.categoryWeight.put(sc, weight);
        }
    }

    public byte getCategoryWeight(ShopCategory sc) {
        return this.categoryWeight.getOrDefault(sc, (byte)0);
    }

    public List<CachedItem> getCachedPermanents() {
        ArrayList<CachedItem> ci = new ArrayList<CachedItem>();
        for (CachedItem c : this.cachedItems) {
            if (!c.getCc().isPermanent()) continue;
            ci.add(c);
        }
        return ci;
    }

    public List<CachedItem> getCachedItems() {
        return this.cachedItems;
    }

    public class CachedItem {
        private CategoryContent cc;
        private int tier = 1;

        public CachedItem(CategoryContent cc) {
            this.cc = cc;
            ShopCache.this.cachedItems.add(this);
            BedWars.debug("New Cached item " + cc.getIdentifier() + " for player " + String.valueOf(ShopCache.this.player));
        }

        public int getTier() {
            return this.tier;
        }

        public CategoryContent getCc() {
            return this.cc;
        }

        public void manageDeath(Arena arena) {
            if (!this.cc.isPermanent()) {
                return;
            }
            if (this.cc.isDowngradable() && this.tier > 1) {
                --this.tier;
            }
            BedWars.debug("ShopCache Item Restore: " + this.cc.getIdentifier() + " for " + String.valueOf(ShopCache.this.player));
            this.cc.giveItems(Bukkit.getPlayer((UUID)ShopCache.this.player), ShopCache.getShopCache(ShopCache.this.player), arena);
        }

        public void upgrade(int slot) {
            ++this.tier;
            Player p = Bukkit.getPlayer((UUID)ShopCache.this.player);
            for (ItemStack i : p.getInventory().getContents()) {
                if (i == null || i.getType() == Material.AIR || !BedWars.nms.getShopUpgradeIdentifier(i).equals(this.cc.getIdentifier())) continue;
                p.getInventory().remove(i);
            }
            this.updateItem(slot, p);
            p.updateInventory();
        }

        public void updateItem(int slot, Player p) {
            if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null) {
                p.getOpenInventory().getTopInventory().setItem(slot, this.cc.getItemStack(Bukkit.getPlayer((UUID)ShopCache.this.player), ShopCache.getShopCache(ShopCache.this.player)));
            }
        }
    }
}

