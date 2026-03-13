/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.gameplay;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.NextEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NextEventChangeEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private IArena arena;
    private NextEvent newEvent;
    private NextEvent oldEvent;

    public NextEventChangeEvent(IArena arena, NextEvent newEvent, NextEvent oldEvent) {
        this.arena = arena;
        this.oldEvent = oldEvent;
        this.newEvent = newEvent;
    }

    public IArena getArena() {
        return this.arena;
    }

    public NextEvent getNewEvent() {
        return this.newEvent;
    }

    public NextEvent getOldEvent() {
        return this.oldEvent;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

