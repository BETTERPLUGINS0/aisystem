/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

@Deprecated
public class VehicleBuyVoucher
extends MTVSubCommand {
    public VehicleBuyVoucher() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!this.player.hasPermission("mtvehicles.buyvoucher")) {
            this.sendMessage(Message.NO_PERMISSION);
            return true;
        }
        if (this.arguments.length != 2) {
            this.sendMessage(Message.USE_BUY_VOUCHER);
            return true;
        }
        if (!DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
            this.sendMessage(Message.ECONOMY_NOT_SET_UP);
            return true;
        }
        if (!DependencyModule.vault.isEconomySetUp()) {
            this.sendMessage(Message.ECONOMY_NOT_SET_UP);
            return true;
        }
        String carUuid = this.arguments[1];
        Double price = VehicleUtils.getPrice(carUuid);
        if (price == null) {
            this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
        }
        if (DependencyModule.vault.withdrawMoneyPlayer((OfflinePlayer)this.player, price)) {
            VehicleUtils.createAndGetItemByUUID((OfflinePlayer)this.player, carUuid);
            ItemStack voucher = ItemUtils.createVoucher(carUuid);
            this.player.getInventory().addItem(new ItemStack[]{voucher});
        }
        return true;
    }
}

