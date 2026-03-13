/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.magmaguy.betterstructures.api;

import com.magmaguy.betterstructures.buildingfitter.FitAnything;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BuildPlaceEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private final FitAnything fitAnything;

    public BuildPlaceEvent(FitAnything fitAnything) {
        this.fitAnything = fitAnything;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    public FitAnything getFitAnything() {
        return this.fitAnything;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

