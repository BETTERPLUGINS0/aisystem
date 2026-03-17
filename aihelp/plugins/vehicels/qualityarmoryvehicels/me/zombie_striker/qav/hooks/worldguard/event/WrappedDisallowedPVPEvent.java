/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.NonNull
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.zombie_striker.qav.hooks.worldguard.event;

import lombok.NonNull;
import me.zombie_striker.qav.hooks.worldguard.event.AbstractWrappedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WrappedDisallowedPVPEvent
extends AbstractWrappedEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player attacker;
    private final Player defender;
    private final Event cause;

    @NonNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public WrappedDisallowedPVPEvent(Player player, Player player2, Event event) {
        this.attacker = player;
        this.defender = player2;
        this.cause = event;
    }

    public Player getAttacker() {
        return this.attacker;
    }

    public Player getDefender() {
        return this.defender;
    }

    public Event getCause() {
        return this.cause;
    }
}

