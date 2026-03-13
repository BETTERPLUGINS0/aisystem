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

public class PlayerXpGainEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private int amount;
    private XpSource xpSource;

    public PlayerXpGainEvent(Player player, int amount, XpSource xpSource) {
        this.player = player;
        this.amount = amount;
        this.xpSource = xpSource;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getAmount() {
        return this.amount;
    }

    public XpSource getXpSource() {
        return this.xpSource;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public static enum XpSource {
        PER_MINUTE,
        PER_TEAMMATE,
        GAME_WIN,
        BED_DESTROYED,
        FINAL_KILL,
        REGULAR_KILL,
        OTHER;

    }
}

