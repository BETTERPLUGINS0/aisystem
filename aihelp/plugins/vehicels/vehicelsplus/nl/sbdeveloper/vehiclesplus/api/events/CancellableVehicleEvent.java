/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 */
package nl.sbdeveloper.vehiclesplus.api.events;

import nl.sbdeveloper.vehiclesplus.api.events.VehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.IVehicle;
import org.bukkit.event.Cancellable;

public class CancellableVehicleEvent<T extends IVehicle>
extends VehicleEvent<T>
implements Cancellable {
    private boolean cancelled;

    protected CancellableVehicleEvent(T t) {
        super(t);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }
}

