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

public class PlayerBedBugSpawnEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final IArena arena;
    private final ITeam playerTeam;

    public PlayerBedBugSpawnEvent(Player p, ITeam playerTeam, IArena arena) {
        this.player = p;
        this.playerTeam = playerTeam;
        this.arena = arena;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return this.player;
    }

    public IArena getArena() {
        return this.arena;
    }

    public ITeam getPlayerTeam() {
        return this.playerTeam;
    }
}

