/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.andrei1058.bedwars.shop.quickbuy;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.CategoryContent;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import com.andrei1058.bedwars.shop.quickbuy.PlayerQuickBuyCache;
import com.andrei1058.bedwars.shop.quickbuy.QuickBuyElement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class QuickBuyTask
extends BukkitRunnable {
    private UUID uuid;

    public QuickBuyTask(UUID uuid) {
        this.uuid = uuid;
        this.runTaskLaterAsynchronously((Plugin)BedWars.plugin, 140L);
    }

    public void run() {
        block10: {
            PlayerQuickBuyCache cache;
            block11: {
                if (Bukkit.getPlayer((UUID)this.uuid) == null) {
                    this.cancel();
                    return;
                }
                if (!Bukkit.getPlayer((UUID)this.uuid).isOnline()) break block10;
                cache = PlayerQuickBuyCache.getQuickBuyCache(this.uuid);
                if (cache == null) {
                    this.cancel();
                    return;
                }
                if (BedWars.getRemoteDatabase().hasQuickBuy(this.uuid)) break block11;
                if (BedWars.shop.getYml().get("quick-buy-defaults") == null) break block10;
                for (String s : BedWars.shop.getYml().getConfigurationSection("quick-buy-defaults").getKeys(false)) {
                    if (BedWars.shop.getYml().get("quick-buy-defaults." + s + ".path") == null || BedWars.shop.getYml().get("quick-buy-defaults." + s + ".slot") == null) continue;
                    try {
                        Integer.valueOf(BedWars.shop.getYml().getString("quick-buy-defaults." + s + ".slot"));
                    } catch (Exception ex) {
                        BedWars.debug(BedWars.shop.getYml().getString("quick-buy-defaults." + s + ".slot") + " must be an integer!");
                        continue;
                    }
                    for (ShopCategory sc : ShopManager.getShop().getCategoryList()) {
                        for (CategoryContent cc : sc.getCategoryContentList()) {
                            if (!cc.getIdentifier().equals(BedWars.shop.getYml().getString("quick-buy-defaults." + s + ".path"))) continue;
                            cache.setElement(Integer.parseInt(BedWars.shop.getYml().getString("quick-buy-defaults." + s + ".slot")), cc);
                        }
                    }
                }
                break block10;
            }
            HashMap<Integer, String> items = BedWars.getRemoteDatabase().getQuickBuySlots(this.uuid, PlayerQuickBuyCache.quickSlots);
            if (items == null) {
                return;
            }
            if (items.isEmpty()) {
                return;
            }
            for (Map.Entry<Integer, String> entry : items.entrySet()) {
                QuickBuyElement e;
                if (entry.getValue().isEmpty() || entry.getValue().equals(" ") || !(e = new QuickBuyElement(entry.getValue(), entry.getKey())).isLoaded()) continue;
                cache.addQuickElement(e);
            }
        }
    }

    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
    }
}

