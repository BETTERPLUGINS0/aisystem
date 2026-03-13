/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.listeners.joinhandler;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.ReJoin;
import com.andrei1058.bedwars.listeners.joinhandler.JoinHandlerCommon;
import com.andrei1058.bedwars.sidebar.SidebarService;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class JoinListenerMultiArena
implements Listener {
    @EventHandler(priority=EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        p.getInventory().setArmorContents(null);
        JoinHandlerCommon.displayCustomerDetails(p);
        if (p.isOp() && Arena.getArenas().isEmpty()) {
            p.performCommand(BedWars.mainCmd);
        }
        ReJoin reJoin = ReJoin.getPlayer(p);
        Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (Arena.isInArena(online)) {
                    BedWars.nms.spigotHidePlayer(online, p);
                    BedWars.nms.spigotHidePlayer(p, online);
                    continue;
                }
                BedWars.nms.spigotShowPlayer(online, p);
                BedWars.nms.spigotShowPlayer(p, online);
            }
            if (reJoin != null) {
                if (reJoin.canReJoin()) {
                    reJoin.reJoin(p);
                    return;
                }
                reJoin.destroy(false);
            }
        }, 14L);
        if (reJoin != null && reJoin.canReJoin()) {
            return;
        }
        Location lobbyLocation = BedWars.config.getConfigLoc("lobbyLoc");
        if (lobbyLocation != null && lobbyLocation.getWorld() != null) {
            TeleportManager.teleport((Entity)p, lobbyLocation);
        }
        Arena.sendLobbyCommandItems(p);
        SidebarService.getInstance().giveSidebar(p, null, true);
        p.setHealthScale(p.getMaxHealth());
        p.setExp(0.0f);
        p.setHealthScale(20.0);
        p.setFoodLevel(20);
    }
}

