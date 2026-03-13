/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerSwapHandItemsEvent
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.support.version.common.VersionCommon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SwapItem
implements Listener {
    @EventHandler
    public void itemSwap(PlayerSwapHandItemsEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (VersionCommon.api.getArenaUtil().isPlaying(e.getPlayer())) {
            if (VersionCommon.api.getArenaUtil().getArenaByPlayer(e.getPlayer()).getStatus() != GameState.playing) {
                e.setCancelled(true);
            }
        } else if (VersionCommon.api.getArenaUtil().isSpectating(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}

