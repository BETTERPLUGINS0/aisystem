/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.listeners.joinhandler;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.listeners.joinhandler.JoinHandlerCommon;
import com.andrei1058.bedwars.sidebar.SidebarService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class JoinListenerShared
implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        JoinHandlerCommon.displayCustomerDetails(p);
        if (p.isOp() && Arena.getArenas().isEmpty()) {
            p.performCommand(BedWars.mainCmd);
        }
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            for (Player inArena : Arena.getArenaByPlayer().keySet()) {
                if (inArena.equals((Object)p)) continue;
                BedWars.nms.spigotHidePlayer(p, inArena);
                BedWars.nms.spigotHidePlayer(inArena, p);
            }
        }, 14L);
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld())) {
            SidebarService.getInstance().giveSidebar(e.getPlayer(), null, true);
        }
    }
}

