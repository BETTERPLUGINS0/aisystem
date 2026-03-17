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

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Deprecated
public class VehicleGiveVoucher
extends MTVSubCommand {
    public VehicleGiveVoucher() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        if (!this.checkPermission("mtvehicles.givevoucher")) {
            return true;
        }
        if (this.arguments.length != 3) {
            this.sendMessage(Message.USE_GIVE_VOUCHER);
            return true;
        }
        Player argPlayer = Bukkit.getPlayer((String)this.arguments[1]);
        String carUuid = this.arguments[2];
        if (argPlayer == null) {
            this.sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }
        ItemStack car = VehicleUtils.createAndGetItemByUUID((OfflinePlayer)argPlayer, carUuid);
        if (car == null) {
            this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
        }
        ItemStack voucher = ItemUtils.createVoucher(carUuid);
        argPlayer.getInventory().addItem(new ItemStack[]{voucher});
        this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_VOUCHER_SUCCESS).replace("%p%", argPlayer.getName()));
        return true;
    }
}

