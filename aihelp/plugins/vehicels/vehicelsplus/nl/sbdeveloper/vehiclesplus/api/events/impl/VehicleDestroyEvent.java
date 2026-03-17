/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import nl.sbdeveloper.vehiclesplus.api.events.CancellableVehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;

public class VehicleDestroyEvent
extends CancellableVehicleEvent<SpawnedVehicle> {
    public VehicleDestroyEvent(SpawnedVehicle spawnedVehicle) {
        super(spawnedVehicle);
    }
}

