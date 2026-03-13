/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.player.PlayerEvent
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.seasons.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class TemperatureEvent
extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final String eventName;

    public TemperatureEvent(@NotNull Player player, String string) {
        super(player);
        this.eventName = string;
    }

    public String eventName() {
        return this.eventName;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

