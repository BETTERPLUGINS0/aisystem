/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.arena.stats;

import com.andrei1058.bedwars.api.arena.stats.DefaultStatistics;
import com.andrei1058.bedwars.api.arena.stats.GameStatsHolder;
import com.andrei1058.bedwars.api.arena.stats.Incrementable;
import com.andrei1058.bedwars.api.arena.stats.PlayerGameStats;
import com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class DefaultStatsHandler
implements Listener {
    @EventHandler(ignoreCancelled=true)
    public void onBedWarsKill(@NotNull PlayerKillEvent event) {
        GameStatsHolder statsHolder = event.getArena().getStatsHolder();
        if (null == statsHolder) {
            return;
        }
        Player killer = event.getKiller();
        Player victim = event.getVictim();
        if (killer != null && !event.getVictimTeam().equals(event.getKillerTeam()) && !victim.equals((Object)killer)) {
            Optional<PlayerGameStats> killerStats = statsHolder.get(killer);
            killerStats.flatMap(stats -> stats.getStatistic(DefaultStatistics.KILLS)).ifPresent(gameStatistic -> {
                if (gameStatistic instanceof Incrementable) {
                    ((Incrementable)((Object)gameStatistic)).increment();
                }
            });
            if (event.getCause().isFinalKill()) {
                killerStats.flatMap(stats -> stats.getStatistic(DefaultStatistics.KILLS_FINAL)).ifPresent(gameStatistic -> {
                    if (gameStatistic instanceof Incrementable) {
                        ((Incrementable)((Object)gameStatistic)).increment();
                    }
                });
            }
        }
        Optional<PlayerGameStats> victimStats = statsHolder.get(victim);
        victimStats.flatMap(stats -> stats.getStatistic(DefaultStatistics.DEATHS)).ifPresent(gameStatistic -> {
            if (gameStatistic instanceof Incrementable) {
                ((Incrementable)((Object)gameStatistic)).increment();
            }
        });
        if (event.getCause().isFinalKill()) {
            victimStats.flatMap(stats -> stats.getStatistic(DefaultStatistics.DEATHS_FINAL)).ifPresent(gameStatistic -> {
                if (gameStatistic instanceof Incrementable) {
                    ((Incrementable)((Object)gameStatistic)).increment();
                }
            });
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onBedWarsBedBreak(@NotNull PlayerBedBreakEvent event) {
        if (null == event.getArena().getStatsHolder()) {
            return;
        }
        event.getArena().getStatsHolder().get(event.getPlayer()).flatMap(stats -> stats.getStatistic(DefaultStatistics.BEDS_DESTROYED)).ifPresent(st -> {
            if (st instanceof Incrementable) {
                ((Incrementable)((Object)st)).increment();
            }
        });
    }
}

