/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.language;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.events.player.PlayerLangChangeEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.sidebar.SidebarService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class LangListener
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR)
    public void onLanguageChangeEvent(PlayerLangChangeEvent e) {
        if (e == null) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (BedWars.config.getLobbyWorldName().equalsIgnoreCase(e.getPlayer().getWorld().getName())) {
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
                Arena.sendLobbyCommandItems(e.getPlayer());
                SidebarService.getInstance().giveSidebar(e.getPlayer(), Arena.getArenaByPlayer(e.getPlayer()), false);
                Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> BedWars.getRemoteDatabase().setLanguage(e.getPlayer().getUniqueId(), e.getNewLang()));
            }, 10L);
        }
    }
}

