/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import nl.sbdeveloper.vehiclesplus.api.events.CancellableVehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.Vehicle;
import org.bukkit.Bukkit;

public class VehicleDeleteEvent
extends CancellableVehicleEvent<Vehicle> {
    public VehicleDeleteEvent(Vehicle vehicle) {
        super(vehicle);
        Bukkit.getLogger().warning("vehicle " + String.valueOf(vehicle.getUuid()) + " has been deleted");
    }
}

