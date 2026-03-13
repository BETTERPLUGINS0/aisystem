/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.player;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBaseLeaveEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private ITeam team;
    private Player p;

    public PlayerBaseLeaveEvent(Player p, ITeam team) {
        this.p = p;
        this.team = team;
    }

    public ITeam getTeam() {
        return this.team;
    }

    public Player getPlayer() {
        return this.p;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

