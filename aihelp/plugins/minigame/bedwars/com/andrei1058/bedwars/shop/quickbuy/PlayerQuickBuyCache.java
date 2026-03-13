/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.shop.quickbuy;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.CategoryContent;
import com.andrei1058.bedwars.shop.quickbuy.QuickBuyElement;
import com.andrei1058.bedwars.shop.quickbuy.QuickBuyTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerQuickBuyCache {
    private final List<QuickBuyElement> elements = new ArrayList<QuickBuyElement>();
    private String emptyItemNamePath;
    private String emptyItemLorePath;
    private ItemStack emptyItem;
    private UUID player;
    private QuickBuyTask task;
    public static int[] quickSlots = new int[]{19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    private static final ConcurrentHashMap<UUID, PlayerQuickBuyCache> quickBuyCaches = new ConcurrentHashMap();
    private final HashMap<Integer, String> updateSlots = new HashMap();

    public PlayerQuickBuyCache(Player player) {
        if (player == null) {
            return;
        }
        this.player = player.getUniqueId();
        this.emptyItem = BedWars.nms.createItemStack(BedWars.shop.getYml().getString("shop-settings.quick-buy-empty-item.material"), BedWars.shop.getYml().getInt("shop-settings.quick-buy-empty-item.amount"), (short)BedWars.shop.getYml().getInt("shop-settings.quick-buy-empty-item.data"));
        if (BedWars.shop.getYml().getBoolean("shop-settings.quick-buy-empty-item.enchanted")) {
            this.emptyItem = ShopManager.enchantItem(this.emptyItem);
        }
        this.emptyItemNamePath = "shop-items-messages.quick-buy-empty-item-name";
        this.emptyItemLorePath = "shop-items-messages.quick-buy-empty-item-lore";
        this.task = new QuickBuyTask(player.getUniqueId());
        quickBuyCaches.put(this.player, this);
    }

    public void addInInventory(Inventory inv, ShopCache shopCache) {
        Player p = Bukkit.getPlayer((UUID)this.player);
        for (QuickBuyElement qbe : this.elements) {
            inv.setItem(qbe.getSlot(), qbe.getCategoryContent().getItemStack(p, shopCache));
        }
        if (this.elements.size() == 21) {
            return;
        }
        ItemStack i = this.getEmptyItem(p);
        for (int x : quickSlots) {
            if (inv.getItem(x) != null) continue;
            inv.setItem(x, i);
        }
    }

    public void destroy() {
        this.elements.clear();
        if (this.task != null) {
            this.task.cancel();
        }
        quickBuyCaches.remove(this.player);
        this.pushChangesToDB();
    }

    public void setElement(int slot, CategoryContent cc) {
        String element;
        this.elements.removeIf(q -> q.getSlot() == slot);
        if (cc == null) {
            element = " ";
        } else {
            this.addQuickElement(new QuickBuyElement(cc.getIdentifier(), slot));
            element = cc.getIdentifier();
        }
        if (this.updateSlots.containsKey(slot)) {
            this.updateSlots.replace(slot, element);
        } else {
            this.updateSlots.put(slot, element);
        }
    }

    @NotNull
    private ItemStack getEmptyItem(Player player) {
        ItemStack i = this.emptyItem.clone();
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.setDisplayName(Language.getMsg(player, this.emptyItemNamePath));
            im.setLore(Language.getList(player, this.emptyItemLorePath));
            i.setItemMeta(im);
        }
        return i;
    }

    public boolean hasCategoryContent(CategoryContent cc) {
        for (QuickBuyElement q : this.getElements()) {
            if (q.getCategoryContent() != cc) continue;
            return true;
        }
        return false;
    }

    @Nullable
    public static PlayerQuickBuyCache getQuickBuyCache(UUID uuid) {
        return quickBuyCaches.getOrDefault(uuid, null);
    }

    public List<QuickBuyElement> getElements() {
        return this.elements;
    }

    public void addQuickElement(QuickBuyElement e) {
        this.elements.add(e);
    }

    public void pushChangesToDB() {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> BedWars.getRemoteDatabase().pushQuickBuyChanges(this.updateSlots, this.player, this.elements));
    }
}

