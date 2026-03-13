/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.team;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamEliminatedEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final IArena arena;
    private final ITeam team;

    public TeamEliminatedEvent(IArena arena, ITeam team) {
        this.arena = arena;
        this.team = team;
    }

    public IArena getArena() {
        return this.arena;
    }

    public ITeam getTeam() {
        return this.team;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

