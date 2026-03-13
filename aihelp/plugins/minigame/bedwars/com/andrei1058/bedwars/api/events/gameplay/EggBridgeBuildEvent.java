/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.gameplay;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EggBridgeBuildEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private TeamColor teamColor;
    private IArena arena;
    private Block block;

    public EggBridgeBuildEvent(TeamColor teamColor, IArena arena, Block block) {
        this.teamColor = teamColor;
        this.arena = arena;
        this.block = block;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Block getBlock() {
        return this.block;
    }

    public TeamColor getTeamColor() {
        return this.teamColor;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

