/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.shop;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.ICategoryContent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShopBuyEvent
extends Event
implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player buyer;
    private final IArena arena;
    private final ICategoryContent categoryContent;
    private boolean cancelled = false;

    @Deprecated
    public ShopBuyEvent(Player buyer, ICategoryContent categoryContent) {
        this.categoryContent = categoryContent;
        this.buyer = buyer;
        this.arena = null;
    }

    public ShopBuyEvent(Player buyer, IArena arena, ICategoryContent categoryContent) {
        this.categoryContent = categoryContent;
        this.buyer = buyer;
        this.arena = arena;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getBuyer() {
        return this.buyer;
    }

    public ICategoryContent getCategoryContent() {
        return this.categoryContent;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

