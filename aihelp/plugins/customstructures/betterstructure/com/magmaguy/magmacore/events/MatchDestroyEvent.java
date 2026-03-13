/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.magmaguy.magmacore.events;

import com.magmaguy.magmacore.instance.MatchEvent;
import com.magmaguy.magmacore.instance.MatchInstance;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchDestroyEvent
extends Event
implements MatchEvent {
    private static final HandlerList handlers = new HandlerList();
    private final MatchInstance matchInstance;

    public MatchDestroyEvent(MatchInstance matchInstance) {
        this.matchInstance = matchInstance;
    }

    @Override
    public MatchInstance getInstance() {
        return this.matchInstance;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}

