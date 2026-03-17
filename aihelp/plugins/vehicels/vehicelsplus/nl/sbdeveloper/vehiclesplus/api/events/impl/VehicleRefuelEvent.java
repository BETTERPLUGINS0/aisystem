/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.events.CancellableVehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;

public class VehicleRefuelEvent
extends CancellableVehicleEvent<SpawnedVehicle> {
    private final String type;
    private final double amount;

    public VehicleRefuelEvent(SpawnedVehicle spawnedVehicle, String string, double d) {
        super(spawnedVehicle);
        this.type = string;
        this.amount = d;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public double getAmount() {
        return this.amount;
    }
}

