/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command.subcommands;

import java.lang.reflect.Field;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.util.ExposeDebug;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DumpVehicleCommand
extends SubCommand {
    public DumpVehicleCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "dumpVehicle";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        return commandSender.hasPermission("qualityarmoryvehicles.debug") ? MessagesConfig.subcommand_debug : null;
    }

    @Override
    public void perform(@NotNull CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.debug")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_ONLY_PLAYERs);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)QualityArmoryVehicles.getPlugin(), () -> {
            Player player = (Player)commandSender;
            VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(player.getVehicle());
            if (vehicleEntity == null) {
                commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_VEHICLE);
                return;
            }
            player.sendMessage(Main.prefix + " You are riding a " + vehicleEntity.getType().getName() + ":");
            if (stringArray.length == 1) {
                try {
                    Field field = vehicleEntity.getClass().getDeclaredField(stringArray[0]);
                    if (!field.isAnnotationPresent(ExposeDebug.class)) {
                        player.sendMessage(Main.prefix + ChatColor.RED + " Field " + stringArray[0] + " not found");
                        return;
                    }
                    field.setAccessible(true);
                    Object object = field.get(vehicleEntity);
                    player.sendMessage(Main.prefix + " " + stringArray[0] + ": " + object);
                } catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
                    player.sendMessage(Main.prefix + ChatColor.RED + " Field " + stringArray[0] + " not found");
                }
            } else {
                for (Field field : vehicleEntity.getClass().getDeclaredFields()) {
                    if (!field.isAnnotationPresent(ExposeDebug.class)) continue;
                    field.setAccessible(true);
                    try {
                        Object object = field.get(vehicleEntity);
                        player.sendMessage(Main.prefix + " " + field.getName() + ": " + object);
                    } catch (IllegalAccessException illegalAccessException) {
                        // empty catch block
                    }
                }
            }
        });
    }
}

