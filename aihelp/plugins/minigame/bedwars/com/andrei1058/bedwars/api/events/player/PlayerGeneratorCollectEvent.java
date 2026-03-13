/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.bukkit.inventory.ItemStack
 */
package com.andrei1058.bedwars.api.events.player;

import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerGeneratorCollectEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Item item;
    private final IArena arena;
    private boolean cancelled = false;

    public PlayerGeneratorCollectEvent(Player player, Item item, IArena arena) {
        this.player = player;
        this.item = item;
        this.arena = arena;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Item getItem() {
        return this.item;
    }

    public ItemStack getItemStack() {
        return this.item.getItemStack();
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

