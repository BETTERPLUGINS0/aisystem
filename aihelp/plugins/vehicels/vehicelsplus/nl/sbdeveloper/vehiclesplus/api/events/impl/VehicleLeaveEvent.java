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
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import org.bukkit.entity.Player;

public class VehicleLeaveEvent
extends CancellableVehicleEvent<DrivableVehicle> {
    private final Player player;
    private final Seat seat;

    public VehicleLeaveEvent(DrivableVehicle drivableVehicle, Player player, Seat seat) {
        super(drivableVehicle);
        this.player = player;
        this.seat = seat;
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }

    @Generated
    public Seat getSeat() {
        return this.seat;
    }
}

