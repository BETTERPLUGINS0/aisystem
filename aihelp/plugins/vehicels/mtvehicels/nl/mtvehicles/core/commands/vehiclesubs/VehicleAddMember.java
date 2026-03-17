/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.List;
import nl.mtvehicles.core.events.VehicleAddMemberEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VehicleAddMember
extends MTVSubCommand {
    public VehicleAddMember() {
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
            this.sendMessage(Message.USE_ADD_MEMBER);
            return true;
        }
        Player argPlayer = Bukkit.getPlayer((String)this.arguments[1]);
        VehicleAddMemberEvent api = new VehicleAddMemberEvent();
        api.setPlayer(this.player);
        api.setAdded(argPlayer);
        api.setLicensePlate(vehicle.getLicensePlate());
        api.call();
        if (api.isCancelled()) {
            return true;
        }
        vehicle = api.getVehicle();
        argPlayer = api.getAdded();
        if (vehicle == null) {
            this.sendMessage(Message.VEHICLE_NOT_FOUND);
            return true;
        }
        if (argPlayer == null) {
            this.sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }
        List<String> members = vehicle.getMembers();
        if (members.contains(playerUUID = argPlayer.getUniqueId().toString())) {
            this.sendMessage(Message.ALREADY_MEMBER);
            return true;
        }
        members.add(playerUUID);
        vehicle.setMembers(members);
        vehicle.save();
        this.sendMessage(Message.MEMBER_CHANGE);
        return true;
    }
}

