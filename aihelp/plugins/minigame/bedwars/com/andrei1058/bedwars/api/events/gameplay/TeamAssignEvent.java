/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.gameplay;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamAssignEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private ITeam team;
    private IArena arena;
    private boolean cancelled = false;

    public TeamAssignEvent(Player player, ITeam team, IArena arena) {
        this.player = player;
        this.team = team;
        this.arena = arena;
    }

    public ITeam getTeam() {
        return this.team;
    }

    public Player getPlayer() {
        return this.player;
    }

    public IArena getArena() {
        return this.arena;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

