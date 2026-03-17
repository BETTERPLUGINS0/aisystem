/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.events.CancellableVehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class VehicleLockStateChangeEvent
extends CancellableVehicleEvent<DrivableVehicle> {
    @Nullable
    private final Player driver;
    private boolean locked;

    public VehicleLockStateChangeEvent(DrivableVehicle drivableVehicle, boolean bl, @Nullable Player player) {
        super(drivableVehicle);
        this.driver = player;
        this.locked = bl;
    }

    @Nullable
    @Generated
    public Player getDriver() {
        return this.driver;
    }

    @Generated
    public boolean isLocked() {
        return this.locked;
    }

    @Generated
    public void setLocked(boolean bl) {
        this.locked = bl;
    }
}

