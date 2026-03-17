/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.List;
import nl.mtvehicles.core.events.VehicleRemoveRiderEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VehicleRemoveRider
extends MTVSubCommand {
    public VehicleRemoveRider() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        String playerUUID;
        Vehicle vehicle = this.getVehicle();
        if (vehicle == null) {
            return true;
        }
        if (this.arguments.length != 2) {
            this.sendMessage(Message.USE_REMOVE_RIDER);
            return true;
        }
        Player argPlayer = Bukkit.getPlayer((String)this.arguments[1]);
        VehicleRemoveRiderEvent api = new VehicleRemoveRiderEvent();
        api.setPlayer(this.player);
        api.setRemoved(argPlayer);
        api.setLicensePlate(vehicle.getLicensePlate());
        api.call();
        if (api.isCancelled()) {
            return true;
        }
        vehicle = api.getVehicle();
        argPlayer = api.getRemoved();
        if (vehicle == null) {
            this.sendMessage(Message.VEHICLE_NOT_FOUND);
            return true;
        }
        if (argPlayer == null) {
            this.sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }
        List<String> riders = vehicle.getRiders();
        if (!riders.contains(playerUUID = argPlayer.getUniqueId().toString())) {
            this.sendMessage(Message.NOT_A_RIDER);
            return true;
        }
        riders.remove(playerUUID);
        vehicle.setRiders(riders);
        vehicle.save();
        this.sendMessage(Message.MEMBER_CHANGE);
        return true;
    }
}

