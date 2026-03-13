/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package com.andrei1058.bedwars.shop.listeners;

import com.andrei1058.bedwars.api.events.player.PlayerJoinArenaEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.shop.ShopCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ShopCacheListener
implements Listener {
    @EventHandler(priority=EventPriority.LOWEST)
    public void onArenaJoin(PlayerJoinArenaEvent e) {
        if (e.isSpectator()) {
            return;
        }
        ShopCache sc = ShopCache.getShopCache(e.getPlayer().getUniqueId());
        if (sc != null) {
            sc.destroy();
        }
        new ShopCache(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onArenaLeave(PlayerLeaveArenaEvent e) {
        ShopCache sc = ShopCache.getShopCache(e.getPlayer().getUniqueId());
        if (sc != null) {
            sc.destroy();
        }
    }

    @EventHandler
    public void onServerLeave(PlayerQuitEvent e) {
        ShopCache sc = ShopCache.getShopCache(e.getPlayer().getUniqueId());
        if (sc != null) {
            sc.destroy();
        }
    }
}

