/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.api.events.spectator;

import com.andrei1058.bedwars.api.arena.IArena;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpectatorTeleportToPlayerEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private UUID spectator;
    private UUID target;
    private IArena arena;
    private boolean cancelled = false;

    public SpectatorTeleportToPlayerEvent(@NotNull Player spectator, @NotNull Player target, IArena arena) {
        this.spectator = spectator.getUniqueId();
        this.target = target.getUniqueId();
        this.arena = arena;
    }

    public Player getSpectator() {
        return Bukkit.getPlayer((UUID)this.spectator);
    }

    public IArena getArena() {
        return this.arena;
    }

    public Player getTarget() {
        return Bukkit.getPlayer((UUID)this.target);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

