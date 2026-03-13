/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.gameplay;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private IArena arena;
    private GameState oldState;
    private GameState newState;

    public GameStateChangeEvent(IArena a, GameState oldState, GameState newState) {
        this.arena = a;
        this.oldState = oldState;
        this.newState = newState;
    }

    public IArena getArena() {
        return this.arena;
    }

    public GameState getOldState() {
        return this.oldState;
    }

    public GameState getNewState() {
        return this.newState;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

