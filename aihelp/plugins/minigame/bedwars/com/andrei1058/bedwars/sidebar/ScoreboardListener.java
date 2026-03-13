/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.sidebar;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent;
import com.andrei1058.bedwars.api.events.player.PlayerJoinArenaEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.api.events.player.PlayerReJoinEvent;
import com.andrei1058.bedwars.api.events.player.PlayerReSpawnEvent;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.sidebar.SidebarService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class ScoreboardListener
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerDamage(@NotNull EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)e.getEntity();
        IArena arena = Arena.getArenaByPlayer(player);
        if (arena == null) {
            return;
        }
        int health = (int)Math.ceil(player.getHealth() - e.getFinalDamage());
        SidebarService.getInstance().refreshHealth(arena, player, health);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onRegain(@NotNull EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)e.getEntity();
        IArena arena = Arena.getArenaByPlayer(player);
        if (arena == null) {
            return;
        }
        int health = (int)Math.ceil(player.getHealth() + e.getAmount());
        SidebarService.getInstance().refreshHealth(arena, player, health);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onReSpawn(@NotNull PlayerReSpawnEvent e) {
        IArena arena = e.getArena();
        SidebarService.getInstance().refreshHealth(arena, e.getPlayer(), (int)Math.ceil(e.getPlayer().getHealth()));
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void reJoin(@NotNull PlayerReJoinEvent e) {
        SidebarService.getInstance().handleReJoin(e.getArena(), e.getPlayer());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void arenaJoin(@NotNull PlayerJoinArenaEvent e) {
        SidebarService.getInstance().handleJoin(e.getArena(), e.getPlayer(), e.isSpectator());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void serverJoin(@NotNull PlayerJoinEvent e) {
        if (BedWars.getServerType() == ServerType.MULTIARENA || BedWars.getServerType() == ServerType.SHARED) {
            SidebarService.getInstance().applyLobbyTab(e.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void arenaLeave(@NotNull PlayerLeaveArenaEvent e) {
        if (BedWars.getServerType() == ServerType.MULTIARENA || BedWars.getServerType() == ServerType.SHARED) {
            SidebarService.getInstance().applyLobbyTab(e.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBedDestroy(@NotNull PlayerBedBreakEvent e) {
        SidebarService.getInstance().refreshPlaceholders(e.getArena());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onFinalKill(@NotNull PlayerKillEvent e) {
        if (!e.getCause().isFinalKill()) {
            return;
        }
        SidebarService.getInstance().refreshPlaceholders(e.getArena());
    }
}

