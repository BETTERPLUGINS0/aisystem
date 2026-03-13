/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Container
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.magmaguy.betterstructures.api;

import org.bukkit.block.Container;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ChestFillEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private Container container;
    private String treasureConfigFilename;

    public ChestFillEvent(Container container, String treasureConfigFilename) {
        this.container = container;
        this.treasureConfigFilename = treasureConfigFilename;
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

    public Container getContainer() {
        return this.container;
    }

    public String getTreasureConfigFilename() {
        return this.treasureConfigFilename;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

