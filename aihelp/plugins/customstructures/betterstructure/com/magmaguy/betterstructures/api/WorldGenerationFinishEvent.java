/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.magmaguy.betterstructures.api;

import com.magmaguy.betterstructures.modules.ModularWorld;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class WorldGenerationFinishEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final ModularWorld modularWorld;

    public WorldGenerationFinishEvent(ModularWorld modularWorld) {
        this.modularWorld = modularWorld;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public ModularWorld getModularWorld() {
        return this.modularWorld;
    }
}

