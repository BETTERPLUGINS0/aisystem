/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.shop.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.CategoryContent;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import com.andrei1058.bedwars.shop.main.ShopIndex;
import com.andrei1058.bedwars.shop.quickbuy.PlayerQuickBuyCache;
import com.andrei1058.bedwars.shop.quickbuy.QuickBuyAdd;
import com.andrei1058.bedwars.shop.quickbuy.QuickBuyElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryListener
implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player)e.getWhoClicked();
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) {
            return;
        }
        if (a.isSpectator(p)) {
            return;
        }
        ShopCache shopCache = ShopCache.getShopCache(p.getUniqueId());
        PlayerQuickBuyCache cache = PlayerQuickBuyCache.getQuickBuyCache(p.getUniqueId());
        if (cache == null) {
            return;
        }
        if (shopCache == null) {
            return;
        }
        if ((ShopIndex.getIndexViewers().contains(p.getUniqueId()) || ShopCategory.getCategoryViewers().contains(p.getUniqueId())) && e.getClickedInventory() != null && e.getClickedInventory().getType().equals((Object)InventoryType.PLAYER)) {
            e.setCancelled(true);
            return;
        }
        if (ShopIndex.getIndexViewers().contains(p.getUniqueId())) {
            e.setCancelled(true);
            for (ShopCategory sc : ShopManager.getShop().getCategoryList()) {
                if (e.getSlot() != sc.getSlot()) continue;
                sc.open(p, ShopManager.getShop(), shopCache);
                return;
            }
            for (QuickBuyElement element : cache.getElements()) {
                if (element.getSlot() != e.getSlot()) continue;
                if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    cache.setElement(element.getSlot(), null);
                    p.closeInventory();
                    return;
                }
                element.getCategoryContent().execute(p, shopCache, element.getSlot());
                return;
            }
        } else if (ShopCategory.getCategoryViewers().contains(p.getUniqueId())) {
            e.setCancelled(true);
            for (ShopCategory sc : ShopManager.getShop().getCategoryList()) {
                if (ShopManager.getShop().getQuickBuyButton().getSlot() == e.getSlot()) {
                    ShopManager.getShop().open(p, cache, false);
                    return;
                }
                if (e.getSlot() == sc.getSlot()) {
                    sc.open(p, ShopManager.getShop(), shopCache);
                    return;
                }
                if (sc.getSlot() != shopCache.getSelectedCategory()) continue;
                for (CategoryContent cc : sc.getCategoryContentList()) {
                    if (cc.getSlot() != e.getSlot()) continue;
                    if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        if (cache.hasCategoryContent(cc)) {
                            return;
                        }
                        new QuickBuyAdd(p, cc);
                        return;
                    }
                    cc.execute(p, shopCache, cc.getSlot());
                    return;
                }
            }
        } else if (QuickBuyAdd.getQuickBuyAdds().containsKey(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
            boolean add = false;
            for (int i : PlayerQuickBuyCache.quickSlots) {
                if (i != e.getSlot()) continue;
                add = true;
            }
            if (!add) {
                return;
            }
            CategoryContent cc = QuickBuyAdd.getQuickBuyAdds().get(e.getWhoClicked().getUniqueId());
            if (cc != null) {
                cache.setElement(e.getSlot(), cc);
            }
            e.getWhoClicked().closeInventory();
        }
    }

    @EventHandler
    public void onUpgradableMove(InventoryClickEvent e) {
        ItemStack i;
        Player p = (Player)e.getWhoClicked();
        ShopCache sc = ShopCache.getShopCache(p.getUniqueId());
        if (sc == null) {
            return;
        }
        if (e.getAction() == InventoryAction.HOTBAR_SWAP && e.getClick() == ClickType.NUMBER_KEY && e.getHotbarButton() > -1 && (i = e.getWhoClicked().getInventory().getItem(e.getHotbarButton())) != null && e.getClickedInventory() != e.getWhoClicked().getInventory() && InventoryListener.shouldCancelMovement(i, sc)) {
            e.setCancelled(true);
        }
        if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
            if (e.getClickedInventory() == null) {
                if (InventoryListener.shouldCancelMovement(e.getCursor(), sc)) {
                    e.getWhoClicked().closeInventory();
                    e.setCancelled(true);
                }
            } else if (e.getClickedInventory().getType() != e.getWhoClicked().getInventory().getType() && InventoryListener.shouldCancelMovement(e.getCursor(), sc)) {
                e.getWhoClicked().closeInventory();
                e.setCancelled(true);
            }
        }
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            if (e.getClickedInventory() == null) {
                if (InventoryListener.shouldCancelMovement(e.getCursor(), sc)) {
                    e.getWhoClicked().closeInventory();
                    e.setCancelled(true);
                }
            } else if (e.getClickedInventory().getType() != e.getWhoClicked().getInventory().getType() && InventoryListener.shouldCancelMovement(e.getCurrentItem(), sc)) {
                e.getWhoClicked().closeInventory();
                e.setCancelled(true);
            }
        }
        if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && InventoryListener.shouldCancelMovement(e.getCurrentItem(), sc)) {
            if (e.getView().getTopInventory().getHolder() != null && e.getInventory().getHolder() == e.getWhoClicked()) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onShopClose(InventoryCloseEvent e) {
        ShopIndex.indexViewers.remove(e.getPlayer().getUniqueId());
        ShopCategory.categoryViewers.remove(e.getPlayer().getUniqueId());
        QuickBuyAdd.quickBuyAdds.remove(e.getPlayer().getUniqueId());
    }

    public static boolean shouldCancelMovement(ItemStack i, ShopCache sc) {
        if (i == null) {
            return false;
        }
        if (sc == null) {
            return false;
        }
        if (BedWars.nms.isCustomBedWarsItem(i) && BedWars.nms.getCustomData(i).equalsIgnoreCase("DEFAULT_ITEM")) {
            return true;
        }
        String identifier = BedWars.nms.getShopUpgradeIdentifier(i);
        if (identifier == null) {
            return false;
        }
        if (identifier.equals("null")) {
            return false;
        }
        ShopCache.CachedItem cachedItem = sc.getCachedItem(identifier);
        return cachedItem != null;
    }
}

