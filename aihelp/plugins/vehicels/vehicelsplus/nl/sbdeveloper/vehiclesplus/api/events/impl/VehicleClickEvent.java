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

public class VehicleClickEvent
extends CancellableVehicleEvent<DrivableVehicle> {
    private final Player clicker;

    public VehicleClickEvent(DrivableVehicle drivableVehicle, Player player) {
        super(drivableVehicle);
        this.clicker = player;
    }

    @Generated
    public Player getClicker() {
        return this.clicker;
    }
}

