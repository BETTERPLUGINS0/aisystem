/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.andrei1058.bedwars.api.events.server;

import com.andrei1058.bedwars.api.server.ISetupSession;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SetupSessionStartEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private ISetupSession setupSession;

    public SetupSessionStartEvent(ISetupSession setupSession) {
        this.setupSession = setupSession;
    }

    public ISetupSession getSetupSession() {
        return this.setupSession;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

