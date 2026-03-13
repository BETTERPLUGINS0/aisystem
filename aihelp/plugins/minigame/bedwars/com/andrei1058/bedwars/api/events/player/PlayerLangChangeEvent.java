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

public class PlayerLangChangeEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private Player player;
    private String oldLang;
    private String newLang;

    public PlayerLangChangeEvent(Player p, String oldLang, String newLang) {
        this.player = p;
        this.oldLang = oldLang;
        this.newLang = newLang;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getOldLang() {
        return this.oldLang;
    }

    public String getNewLang() {
        return this.newLang;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

