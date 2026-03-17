/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 */
package nl.sbdeveloper.vehiclesplus.api.events;

import nl.sbdeveloper.vehiclesplus.api.events.Event;
import org.bukkit.event.Cancellable;

public class CancellableEvent
extends Event
implements Cancellable {
    private boolean cancelled;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }
}

