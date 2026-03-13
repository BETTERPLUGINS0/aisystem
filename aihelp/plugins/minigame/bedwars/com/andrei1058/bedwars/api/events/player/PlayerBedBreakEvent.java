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
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBedBreakEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final IArena arena;
    private final ITeam playerTeam;
    private final ITeam victimTeam;
    private Function<Player, String> message;
    private Function<Player, String> title;
    private Function<Player, String> subTitle;

    public PlayerBedBreakEvent(Player p, ITeam playerTeam, ITeam victimTeam, IArena arena, Function<Player, String> message, Function<Player, String> title, Function<Player, String> subTitle) {
        this.player = p;
        this.playerTeam = playerTeam;
        this.victimTeam = victimTeam;
        this.arena = arena;
        this.message = message;
        this.title = title;
        this.subTitle = subTitle;
    }

    public ITeam getPlayerTeam() {
        return this.playerTeam;
    }

    public ITeam getVictimTeam() {
        return this.victimTeam;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Function<Player, String> getMessage() {
        return this.message;
    }

    public void setMessage(Function<Player, String> message) {
        this.message = message;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Function<Player, String> getTitle() {
        return this.title;
    }

    public void setTitle(Function<Player, String> title) {
        this.title = title;
    }

    public Function<Player, String> getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(Function<Player, String> subTitle) {
        this.subTitle = subTitle;
    }
}

