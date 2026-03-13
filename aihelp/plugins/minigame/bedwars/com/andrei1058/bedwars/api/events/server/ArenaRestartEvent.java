/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaRestartEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private String arena;
    private String worldName;

    public ArenaRestartEvent(String arena, String worldName) {
        this.arena = arena;
        this.worldName = worldName;
    }

    public String getArenaName() {
        return this.arena;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

