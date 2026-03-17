/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleGiveFuel
extends MTVSubCommand {
    public VehicleGiveFuel() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        if (!this.checkPermission("mtvehicles.givefuel")) {
            return true;
        }
        if (this.arguments.length != 3) {
            this.sendMessage(Message.USE_GIVE_FUEL);
            return true;
        }
        Player argPlayer = Bukkit.getPlayer((String)this.arguments[1]);
        int fuel = 25;
        try {
            fuel = Integer.parseInt(this.arguments[2]);
        } catch (Exception e) {
            this.sendMessage(Message.USE_GIVE_FUEL);
            return true;
        }
        if (argPlayer == null) {
            this.sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }
        ItemStack fuelItem = VehicleFuel.jerrycanItem(fuel, fuel);
        argPlayer.getInventory().addItem(new ItemStack[]{fuelItem});
        this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_FUEL_SUCCESS).replace("%p%", argPlayer.getName()).replace("%l%", String.valueOf(fuel)));
        return false;
    }
}

