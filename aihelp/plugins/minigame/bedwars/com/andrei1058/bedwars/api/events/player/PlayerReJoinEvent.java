/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.player;

import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerReJoinEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private IArena arena;
    private int respawnTime;
    private boolean cancelled = false;

    public PlayerReJoinEvent(Player player, IArena arena, int respawnTime) {
        this.player = player;
        this.arena = arena;
        this.respawnTime = respawnTime;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public int getRespawnTime() {
        return this.respawnTime;
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

