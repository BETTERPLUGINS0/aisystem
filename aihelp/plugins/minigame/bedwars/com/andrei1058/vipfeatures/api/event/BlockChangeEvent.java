/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.vipfeatures.api.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlockChangeEvent
extends Event {
    private static HandlerList handlerList = new HandlerList();
    private Location location;
    private Material oldMaterial;
    private Material newMaterial;
    private boolean cancelled = false;

    public BlockChangeEvent(Location location, Material oldMaterial, Material newMaterial) {
        this.location = location;
        this.oldMaterial = oldMaterial;
        this.newMaterial = newMaterial;
    }

    public Location getLocation() {
        return this.location;
    }

    public Material getOldMaterial() {
        return this.oldMaterial;
    }

    public Material getNewMaterial() {
        return this.newMaterial;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

