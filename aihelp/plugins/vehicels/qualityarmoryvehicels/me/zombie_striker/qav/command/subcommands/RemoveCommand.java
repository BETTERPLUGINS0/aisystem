/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command.subcommands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.UnlockedVehicle;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemoveCommand
extends SubCommand {
    public RemoveCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "removeVehicle";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        return commandSender.hasPermission("qualityarmoryvehicles.removevehicle") ? MessagesConfig.subcommand_RemoveVehicle : null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.removevehicle")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        if (stringArray.length == 0) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(Main.prefix + "&7 Try to use &6/qav removeVehicle <type> [player]")));
            return;
        }
        OfflinePlayer offlinePlayer = stringArray.length == 2 ? Bukkit.getOfflinePlayer((String)stringArray[1]) : null;
        List list = Main.vehicles.stream().filter(vehicleEntity -> vehicleEntity.getType().getName().equalsIgnoreCase(stringArray[0])).filter(vehicleEntity -> offlinePlayer == null || offlinePlayer.getUniqueId().equals(vehicleEntity.getOwner())).collect(Collectors.toList());
        for (VehicleEntity vehicleEntity2 : list) {
            vehicleEntity2.deconstruct(null, "Remove command");
        }
        if (offlinePlayer == null) {
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)QualityArmoryVehicles.getPlugin(), () -> {
                List<File> list = QualityArmoryVehicles.getUnlockedVehiclesFiles();
                for (File file : list) {
                    List<UnlockedVehicle> list2 = QualityArmoryVehicles.parseUnlockedVehicles(file);
                    list2.removeIf(unlockedVehicle -> unlockedVehicle.getVehicleType().getName().equalsIgnoreCase(stringArray[0]));
                    QualityArmoryVehicles.setUnlockedVehicles(file, list2);
                }
            });
        } else {
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)QualityArmoryVehicles.getPlugin(), () -> {
                File file = QualityArmoryVehicles.getUnlockedVehiclesFile(offlinePlayer);
                List<UnlockedVehicle> list = QualityArmoryVehicles.parseUnlockedVehicles(file);
                list.removeIf(unlockedVehicle -> unlockedVehicle.getVehicleType().getName().equalsIgnoreCase(stringArray[0]));
                QualityArmoryVehicles.setUnlockedVehicles(file, list);
            });
        }
    }

    @Override
    public List<String> complete(CommandSender commandSender, String[] stringArray) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (AbstractVehicle abstractVehicle : Main.vehicleTypes) {
            if (!abstractVehicle.getName().toLowerCase().startsWith(stringArray[0].toLowerCase())) continue;
            arrayList.add(abstractVehicle.getName());
        }
        return arrayList;
    }
}

