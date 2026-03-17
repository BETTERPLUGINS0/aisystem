/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.inventory.ItemStack;

public class VehicleRefill
extends MTVSubCommand {
    public VehicleRefill() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!this.checkPermission("mtvehicles.refill")) {
            return true;
        }
        ItemStack item = this.player.getInventory().getItemInMainHand();
        if (!this.isHoldingVehicle()) {
            return true;
        }
        String licensePlate = VehicleUtils.getLicensePlate(item);
        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        vehicle.setFuel(100.0);
        vehicle.save();
        VehicleData.fuel.put(licensePlate, 100.0);
        if (VehicleData.fallDamage.get(licensePlate) != null) {
            VehicleData.fallDamage.remove(licensePlate);
        }
        this.sendMessage(Message.REFILL_SUCCESSFUL);
        return true;
    }
}

