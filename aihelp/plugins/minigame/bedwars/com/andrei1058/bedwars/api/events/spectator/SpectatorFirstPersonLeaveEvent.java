/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.spectator;

import com.andrei1058.bedwars.api.arena.IArena;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpectatorFirstPersonLeaveEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Player spectator;
    private IArena arena;
    private Function<Player, String> title;
    private Function<Player, String> subTitle;
    private int fadeIn = 0;
    private int stay = 40;
    private int fadeOut = 10;

    public SpectatorFirstPersonLeaveEvent(Player spectator, IArena arena, Function<Player, String> title, Function<Player, String> subtitle) {
        this.spectator = spectator;
        this.arena = arena;
        this.title = title;
        this.subTitle = subtitle;
    }

    public Player getSpectator() {
        return this.spectator;
    }

    public IArena getArena() {
        return this.arena;
    }

    public Function<Player, String> getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(Function<Player, String> subTitle) {
        this.subTitle = subTitle;
    }

    public Function<Player, String> getTitle() {
        return this.title;
    }

    public void setTitle(Function<Player, String> title) {
        this.title = title;
    }

    public int getFadeIn() {
        return this.fadeIn;
    }

    public int getFadeOut() {
        return this.fadeOut;
    }

    public int getStay() {
        return this.stay;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

