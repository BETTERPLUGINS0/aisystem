/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.NonNull
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.zombie_striker.qav.hooks.worldguard.event;

import lombok.NonNull;
import me.zombie_striker.qav.hooks.worldguard.event.AbstractWrappedEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WrappedDamageEntityEvent
extends AbstractWrappedEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Event originalEvent;
    private final Player player;
    private final Location target;
    private final Entity entity;

    @NonNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public WrappedDamageEntityEvent(Event event, Player player, Location location, Entity entity) {
        this.originalEvent = event;
        this.player = player;
        this.target = location;
        this.entity = entity;
    }

    public Event getOriginalEvent() {
        return this.originalEvent;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Location getTarget() {
        return this.target;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

