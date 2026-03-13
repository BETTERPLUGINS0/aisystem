/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerPreLoginEvent
 *  org.bukkit.event.player.AsyncPlayerPreLoginEvent$Result
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.stats;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.stats.PlayerStats;
import java.time.Instant;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class StatsListener
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR)
    public void onAsyncPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        PlayerStats stats = BedWars.getRemoteDatabase().fetchStats(event.getUniqueId());
        stats.setName(event.getName());
        BedWars.getStatsManager().put(event.getUniqueId(), stats);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            BedWars.getStatsManager().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onBedBreak(PlayerBedBreakEvent event) {
        PlayerStats stats = BedWars.getStatsManager().get(event.getPlayer().getUniqueId());
        stats.setBedsDestroyed(stats.getBedsDestroyed() + 1);
    }

    @EventHandler
    public void onPlayerKill(PlayerKillEvent event) {
        PlayerStats killerStats;
        PlayerStats victimStats = BedWars.getStatsManager().get(event.getVictim().getUniqueId());
        PlayerStats playerStats = !event.getVictim().equals((Object)event.getKiller()) ? (event.getKiller() == null ? null : BedWars.getStatsManager().getUnsafe(event.getKiller().getUniqueId())) : (killerStats = null);
        if (event.getCause().isFinalKill()) {
            victimStats.setFinalDeaths(victimStats.getFinalDeaths() + 1);
            victimStats.setLosses(victimStats.getLosses() + 1);
            victimStats.setGamesPlayed(victimStats.getGamesPlayed() + 1);
            if (killerStats != null) {
                killerStats.setFinalKills(killerStats.getFinalKills() + 1);
            }
        } else {
            victimStats.setDeaths(victimStats.getDeaths() + 1);
            if (killerStats != null) {
                killerStats.setKills(killerStats.getKills() + 1);
            }
        }
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        for (UUID uuid : event.getWinners()) {
            Player player = Bukkit.getPlayer((UUID)uuid);
            if (player == null || !player.isOnline()) continue;
            PlayerStats stats = BedWars.getStatsManager().get(uuid);
            stats.setWins(stats.getWins() + 1);
            IArena playerArena = Arena.getArenaByPlayer(player);
            if (playerArena == null || !playerArena.equals(event.getArena())) continue;
            stats.setGamesPlayed(stats.getGamesPlayed() + 1);
        }
    }

    @EventHandler
    public void onArenaLeave(@NotNull PlayerLeaveArenaEvent event) {
        Player player = event.getPlayer();
        ITeam team = event.getArena().getExTeam(player.getUniqueId());
        if (team == null) {
            return;
        }
        if (event.getArena().getStatus() == GameState.starting || event.getArena().getStatus() == GameState.waiting) {
            return;
        }
        PlayerStats playerStats = BedWars.getStatsManager().getUnsafe(player.getUniqueId());
        if (playerStats == null) {
            return;
        }
        Instant now = Instant.now();
        playerStats.setLastPlay(now);
        if (playerStats.getFirstPlay() == null) {
            playerStats.setFirstPlay(now);
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)BedWars.plugin, () -> BedWars.getRemoteDatabase().saveStats(playerStats), 10L);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onQuit(@NotNull PlayerQuitEvent event) {
        BedWars.getStatsManager().remove(event.getPlayer().getUniqueId());
    }
}

