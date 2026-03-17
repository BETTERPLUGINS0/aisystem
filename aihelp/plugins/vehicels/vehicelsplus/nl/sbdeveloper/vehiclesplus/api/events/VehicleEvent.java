/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.events;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.events.Event;
import nl.sbdeveloper.vehiclesplus.api.vehicles.IVehicle;

public class VehicleEvent<T extends IVehicle>
extends Event {
    private final T vehicle;

    protected VehicleEvent(T t) {
        this.vehicle = t;
    }

    @Generated
    public T getVehicle() {
        return this.vehicle;
    }
}

