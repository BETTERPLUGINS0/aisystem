/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package com.andrei1058.bedwars.shop.quickbuy;

import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.CategoryContent;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import com.andrei1058.bedwars.shop.quickbuy.PlayerQuickBuyCache;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class QuickBuyAdd {
    public static HashMap<UUID, CategoryContent> quickBuyAdds = new HashMap();

    public QuickBuyAdd(Player player, CategoryContent cc) {
        ShopCategory.categoryViewers.remove(player.getUniqueId());
        this.open(player, cc);
    }

    public void open(Player player, CategoryContent cc) {
        Inventory inv = Bukkit.createInventory(null, (int)ShopManager.getShop().getInvSize(), (String)Language.getMsg(player, "shop-items-messages.quick-buy-add-inventory-name"));
        PlayerQuickBuyCache cache = PlayerQuickBuyCache.getQuickBuyCache(player.getUniqueId());
        ShopCache sc = ShopCache.getShopCache(player.getUniqueId());
        if (sc == null || cache == null) {
            player.closeInventory();
        }
        inv.setItem(4, cc.getItemStack(player, Objects.requireNonNull(sc)));
        Objects.requireNonNull(cache).addInInventory(inv, sc);
        player.openInventory(inv);
        quickBuyAdds.put(player.getUniqueId(), cc);
    }

    public static HashMap<UUID, CategoryContent> getQuickBuyAdds() {
        return new HashMap<UUID, CategoryContent>(quickBuyAdds);
    }
}

