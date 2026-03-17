/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class AbstractWrappedEvent
extends Event
implements Cancellable {
    private Event.Result result = Event.Result.DEFAULT;

    public boolean isCancelled() {
        return this.result == Event.Result.DENY;
    }

    public void setCancelled(boolean cancel) {
        if (cancel) {
            this.setResult(Event.Result.DENY);
        }
    }

    public void setResult(Event.Result result) {
        this.result = result;
    }

    public Event.Result getResult() {
        return this.result;
    }
}

