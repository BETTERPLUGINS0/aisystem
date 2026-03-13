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
import com.andrei1058.bedwars.api.arena.team.ITeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerReSpawnEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private IArena arena;
    private ITeam team;

    public PlayerReSpawnEvent(Player player, IArena arena, ITeam team) {
        this.player = player;
        this.arena = arena;
        this.team = team;
    }

    public IArena getArena() {
        return this.arena;
    }

    public ITeam getTeam() {
        return this.team;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

