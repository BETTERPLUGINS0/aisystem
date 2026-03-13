/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.magmaguy.magmacore.events;

import com.magmaguy.magmacore.instance.MatchEvent;
import com.magmaguy.magmacore.instance.MatchInstance;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchJoinEvent
extends Event
implements MatchEvent,
Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final MatchInstance matchInstance;
    private final Player player;
    private boolean cancelled = false;

    public MatchJoinEvent(MatchInstance matchInstance, Player player) {
        this.matchInstance = matchInstance;
        this.player = player;
    }

    @Override
    public MatchInstance getInstance() {
        return this.matchInstance;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }
}

