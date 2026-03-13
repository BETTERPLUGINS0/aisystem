/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.gameplay;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private List<UUID> winners;
    private List<UUID> losers;
    private List<UUID> aliveWinners;
    private ITeam teamWinner;
    private IArena arena;

    public GameEndEvent(IArena arena, List<UUID> winners, List<UUID> losers, ITeam teamWinner, List<UUID> aliveWinners) {
        this.winners = new ArrayList<UUID>(winners);
        this.arena = arena;
        this.losers = new ArrayList<UUID>(losers);
        this.teamWinner = teamWinner;
        this.aliveWinners = new ArrayList<UUID>(aliveWinners);
    }

    public List<UUID> getWinners() {
        return this.winners;
    }

    public ITeam getTeamWinner() {
        return this.teamWinner;
    }

    public List<UUID> getLosers() {
        return this.losers;
    }

    public IArena getArena() {
        return this.arena;
    }

    public List<UUID> getAliveWinners() {
        return this.aliveWinners;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

