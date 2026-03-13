/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.shop;

import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShopOpenEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private final IArena arena;
    private boolean cancelled = false;

    @Deprecated
    public ShopOpenEvent(Player player) {
        this.player = player;
        this.arena = null;
    }

    public ShopOpenEvent(Player player, IArena arena) {
        this.player = player;
        this.arena = arena;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

