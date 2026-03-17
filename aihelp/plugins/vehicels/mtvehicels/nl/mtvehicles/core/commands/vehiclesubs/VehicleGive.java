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

import java.util.HashMap;
import nl.mtvehicles.core.commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleGive
extends MTVSubCommand {
    public VehicleGive() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        ItemStack itemToGive;
        if (!this.sender.hasPermission("mtvehicles.givecar") && !this.sender.hasPermission("mtvehicles.givevoucher")) {
            ConfigModule.messagesConfig.sendMessage(this.sender, Message.NO_PERMISSION);
            return true;
        }
        if (this.arguments.length != 3 && this.arguments.length != 4) {
            this.sendMessage(Message.USE_NEW_VEHICLE_GIVE);
            return true;
        }
        Player argPlayer = Bukkit.getPlayer((String)this.arguments[1]);
        if (argPlayer == null) {
            this.sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }
        HashMap<String, String> vehicleList = VehicleTabCompleterManager.getVehicleList();
        if (!vehicleList.containsKey(this.arguments[2])) {
            this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
        }
        String carUuid = vehicleList.get(this.arguments[2]);
        boolean useVoucher = this.arguments.length < 4 ? false : this.arguments[3].equals("--voucher:true");
        if (useVoucher) {
            if (!this.checkPermission("mtvehicles.givevoucher")) {
                return true;
            }
            if (VehicleUtils.getItem(carUuid) == null) {
                this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
                return true;
            }
            itemToGive = ItemUtils.createVoucher(carUuid);
        } else {
            if (!this.checkPermission("mtvehicles.givecar")) {
                return true;
            }
            ItemStack car = VehicleUtils.createAndGetItemByUUID((OfflinePlayer)argPlayer, carUuid);
            if (car == null) {
                this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
                return true;
            }
            itemToGive = car;
        }
        HashMap failedItems = argPlayer.getInventory().addItem(new ItemStack[]{itemToGive});
        if (!failedItems.isEmpty()) {
            this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.NO_INVENTORY_SPACE));
            return true;
        }
        if (useVoucher) {
            this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_VOUCHER_SUCCESS).replace("%p%", argPlayer.getName()));
        } else {
            this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_SUCCESS).replace("%p%", argPlayer.getName()));
        }
        return true;
    }
}

