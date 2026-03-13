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

public class ArenaDisableEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private String arenaName;
    private String worldName;

    public ArenaDisableEvent(String arenaName, String worldName) {
        this.arenaName = arenaName;
        this.worldName = worldName;
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

