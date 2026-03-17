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
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import org.bukkit.entity.Player;

public class VehicleBuyEvent
extends CancellableVehicleEvent<VehicleModel> {
    private final Player buyingPlayer;

    public VehicleBuyEvent(VehicleModel vehicleModel, Player player) {
        super(vehicleModel);
        this.buyingPlayer = player;
    }

    @Generated
    public Player getBuyingPlayer() {
        return this.buyingPlayer;
    }
}

