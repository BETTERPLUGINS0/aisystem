/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;

public class VehiclePrivate
extends MTVSubCommand {
    public VehiclePrivate() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!this.player.isInsideVehicle()) {
            Vehicle vehicle = this.getVehicle();
            if (vehicle == null) {
                return true;
            }
            vehicle.setPublic(false);
            vehicle.save();
            this.sendMessage(Message.ACTION_SUCCESSFUL);
            return true;
        }
        return false;
    }
}

