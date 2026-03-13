/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAfkEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private AFKType afkType;

    public PlayerAfkEvent(Player player, AFKType afkType) {
        this.afkType = afkType;
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public AFKType getAfkType() {
        return this.afkType;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public static enum AFKType {
        START,
        END;

    }
}

