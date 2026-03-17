/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.player.PlayerEvent
 */
package me.m56738.smoothcoasters.api.event;

import me.m56738.smoothcoasters.api.implementation.Implementation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerSmoothCoastersHandshakeEvent
extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList();
    private final Implementation implementation;
    private final String version;

    public PlayerSmoothCoastersHandshakeEvent(Player player, Implementation implementation, String string) {
        super(player);
        this.implementation = implementation;
        this.version = string;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public byte getImplementationVersion() {
        return this.implementation.getVersion();
    }

    public String getVersion() {
        return this.version;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}

