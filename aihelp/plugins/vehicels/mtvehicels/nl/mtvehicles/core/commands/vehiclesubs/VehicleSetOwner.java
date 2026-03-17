/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.ArrayList;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleSetOwner
extends MTVSubCommand {
    public VehicleSetOwner() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        ItemStack item = this.player.getInventory().getItemInMainHand();
        boolean playerSetOwner = (Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.PUT_ONESELF_AS_OWNER);
        if (!playerSetOwner && !this.checkPermission("mtvehicles.setowner")) {
            return true;
        }
        if (!this.isHoldingVehicle()) {
            return true;
        }
        if (this.arguments.length != 2) {
            this.sendMessage(Message.USE_SET_OWNER);
            return true;
        }
        String licensePlate = VehicleUtils.getLicensePlate(item);
        if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            this.sendMessage(Message.VEHICLE_NOT_FOUND);
            return true;
        }
        Player argPlayer = Bukkit.getPlayer((String)this.arguments[1]);
        if (argPlayer == null) {
            this.sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }
        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        assert (vehicle != null);
        if (!(!playerSetOwner && this.player.hasPermission("mtvehicles.setowner") || vehicle.isOwner((OfflinePlayer)this.player))) {
            this.sendMessage(Message.NOT_YOUR_CAR);
            return true;
        }
        vehicle.setRiders(new ArrayList<String>());
        vehicle.setMembers(new ArrayList<String>());
        vehicle.setOwner(argPlayer.getUniqueId());
        vehicle.save();
        this.sendMessage(Message.MEMBER_CHANGE);
        return true;
    }
}

