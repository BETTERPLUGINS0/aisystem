/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.api.events.player;

import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

public class PlayerLeaveArenaEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private boolean spectator;
    private IArena arena;
    @Nullable
    private Player lastDamager;

    public PlayerLeaveArenaEvent(Player p, IArena arena, @Nullable Player lastDamager) {
        this.player = p;
        this.spectator = arena.isSpectator(p);
        this.arena = arena;
        this.lastDamager = lastDamager;
    }

    @Deprecated
    public PlayerLeaveArenaEvent(Player p, IArena arena) {
        this(p, arena, null);
    }

    public Player getPlayer() {
        return this.player;
    }

    public IArena getArena() {
        return this.arena;
    }

    public boolean isSpectator() {
        return this.spectator;
    }

    @Nullable
    public Player getLastDamager() {
        return this.lastDamager;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

