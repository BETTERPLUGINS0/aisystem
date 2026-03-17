/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package nl.sbdeveloper.vehiclesplus.api.events;

import lombok.Generated;
import org.bukkit.event.HandlerList;

public class Event
extends org.bukkit.event.Event {
    private static final HandlerList handlerList = new HandlerList();

    public HandlerList getHandlers() {
        return handlerList;
    }

    @Generated
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

