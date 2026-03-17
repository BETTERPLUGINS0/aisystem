/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.infrastructure.models;

import javax.annotation.Nullable;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MTVSubCommand {
    protected CommandSender sender;
    @Nullable
    protected Player player;
    protected boolean isPlayer;
    protected String[] arguments;
    private boolean isPlayerCommand;

    public boolean onExecute(CommandSender sender, Command cmd, String s, String[] args) {
        this.sender = sender;
        this.isPlayer = sender instanceof Player;
        this.player = this.isPlayer ? (Player)sender : null;
        this.arguments = args;
        if (this.isPlayerCommand && !this.isPlayer) {
            this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.NOT_FOR_CONSOLE));
            return true;
        }
        return this.execute();
    }

    public abstract boolean execute();

    public void sendMessage(String message) {
        this.sender.sendMessage(TextUtils.colorize(message));
    }

    public void sendMessage(Message message) {
        this.sender.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(message)));
    }

    public boolean checkPermission(String permission) {
        if (this.sender.hasPermission(permission)) {
            return true;
        }
        ConfigModule.messagesConfig.sendMessage(this.sender, Message.NO_PERMISSION);
        return false;
    }

    public boolean isPlayerCommand() {
        return this.isPlayerCommand;
    }

    public void setPlayerCommand(boolean playerCommand) {
        this.isPlayerCommand = playerCommand;
    }

    protected Vehicle getVehicle() {
        Vehicle vehicle;
        if (this.player == null) {
            return null;
        }
        if (VehicleUtils.isInsideVehicle(this.player) && (vehicle = VehicleUtils.getVehicle(VehicleUtils.getLicensePlate(this.player.getVehicle()))).isOwner((OfflinePlayer)this.player)) {
            return vehicle;
        }
        ItemStack item = this.player.getInventory().getItemInMainHand();
        if (item.hasItemMeta() && new NBTItem(item).hasKey("mtvehicles.kenteken").booleanValue()) {
            return VehicleUtils.getVehicle(VehicleUtils.getLicensePlate(item));
        }
        this.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.COMMAND_NO_VEHICLE)));
        return null;
    }

    protected boolean isHoldingVehicle() {
        ItemStack item = this.player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !new NBTItem(item).hasKey("mtvehicles.kenteken").booleanValue()) {
            this.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.NO_VEHICLE_IN_HAND)));
            return false;
        }
        return true;
    }
}

