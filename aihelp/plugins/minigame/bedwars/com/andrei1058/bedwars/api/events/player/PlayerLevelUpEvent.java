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

public class PlayerLevelUpEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private int newXpTarget;
    private int newLevel;

    public PlayerLevelUpEvent(Player player, int newLevel, int levelUpXp) {
        this.player = player;
        this.newLevel = newLevel;
        this.newXpTarget = levelUpXp;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    public int getNewXpTarget() {
        return this.newXpTarget;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

