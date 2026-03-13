/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.player;

import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinArenaEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private boolean spectator;
    private boolean cancelled = false;
    private IArena arena;

    public PlayerJoinArenaEvent(IArena arena, Player p, boolean spectator) {
        this.arena = arena;
        this.player = p;
        this.spectator = spectator;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isSpectator() {
        return this.spectator;
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

