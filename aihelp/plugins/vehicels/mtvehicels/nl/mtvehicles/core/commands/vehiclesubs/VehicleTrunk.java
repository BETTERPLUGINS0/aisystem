/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.VehicleOpenTrunkEvent;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;

public class VehicleTrunk
extends MTVSubCommand {
    public VehicleTrunk() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        Vehicle vehicle = this.getVehicle();
        if (vehicle == null) {
            return true;
        }
        VehicleOpenTrunkEvent api = new VehicleOpenTrunkEvent();
        api.setPlayer(this.player);
        api.setLicensePlate(vehicle.getLicensePlate());
        api.call();
        if (api.isCancelled()) {
            return true;
        }
        VehicleUtils.openTrunk(this.player, api.getLicensePlate());
        return true;
    }
}

