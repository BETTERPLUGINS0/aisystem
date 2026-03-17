/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.command.CommandSender
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.events.CancellableVehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class VehicleGiveEvent
extends CancellableVehicleEvent<VehicleModel> {
    private final CommandSender sender;
    private final String garage;
    private final Source source;

    public VehicleGiveEvent(@NotNull VehicleModel vehicleModel, @NotNull CommandSender commandSender, @NotNull Garage garage, @NotNull Source source) {
        super(vehicleModel);
        this.sender = commandSender;
        this.garage = garage.getName();
        this.source = source;
    }

    @Generated
    public CommandSender getSender() {
        return this.sender;
    }

    @Generated
    public String getGarage() {
        return this.garage;
    }

    @Generated
    public Source getSource() {
        return this.source;
    }

    public static enum Source {
        COMMAND,
        VOUCHER;

    }
}

