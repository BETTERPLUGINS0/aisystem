/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.events.CancellableVehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;

public class VehicleUpgradeEvent
extends CancellableVehicleEvent<DrivableVehicle> {
    private final UpgradeType type;

    public VehicleUpgradeEvent(DrivableVehicle drivableVehicle, UpgradeType upgradeType) {
        super(drivableVehicle);
        this.type = upgradeType;
    }

    @Generated
    public UpgradeType getType() {
        return this.type;
    }

    public static enum UpgradeType {
        SPEED,
        TANK,
        ACCELERATION,
        TURNING;

    }
}

