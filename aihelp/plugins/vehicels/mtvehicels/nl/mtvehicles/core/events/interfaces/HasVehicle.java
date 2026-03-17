/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.events.interfaces;

import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;

public interface HasVehicle {
    public String getLicensePlate();

    default public Vehicle getVehicle() {
        return VehicleUtils.getVehicle(this.getLicensePlate());
    }
}

