/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.server;

import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaEnableEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private IArena arena;

    public ArenaEnableEvent(IArena a) {
        this.arena = a;
    }

    public IArena getArena() {
        return this.arena;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

