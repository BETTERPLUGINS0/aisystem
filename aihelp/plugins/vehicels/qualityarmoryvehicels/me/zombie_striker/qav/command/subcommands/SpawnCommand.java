/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command.subcommands;

import java.util.ArrayList;
import java.util.List;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpawnCommand
extends SubCommand {
    public SpawnCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "spawnVehicle";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        return commandSender.hasPermission("qualityarmoryvehicles.spawnvehicle") ? MessagesConfig.subcommand_removeNearbyVehicles : null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.spawnvehicle")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        if (stringArray.length < 1) {
            commandSender.sendMessage(Main.prefix + "Usage /qav " + this.getName() + " <vehicle> [x] [y] [z]");
            return;
        }
        String string = stringArray[0];
        Location location = null;
        if (stringArray.length == 4) {
            try {
                location = new Location((World)Bukkit.getWorlds().get(0), Double.parseDouble(stringArray[1]), Double.parseDouble(stringArray[2]), Double.parseDouble(stringArray[3]));
            } catch (NumberFormatException numberFormatException) {}
        } else if (commandSender instanceof Player) {
            location = ((Player)commandSender).getLocation();
        } else {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_ONLY_PLAYERs);
            return;
        }
        AbstractVehicle abstractVehicle = QualityArmoryVehicles.getVehicle(string);
        if (abstractVehicle == null) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_VALID_VEHICLE);
            return;
        }
        assert (location != null);
        QualityArmoryVehicles.spawnVehicle(abstractVehicle, location, commandSender instanceof Player ? (Player)commandSender : null);
    }

    @Override
    public List<String> complete(CommandSender commandSender, String[] stringArray) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (stringArray.length == 0 || stringArray.length == 1) {
            for (AbstractVehicle abstractVehicle : Main.vehicleTypes) {
                if (!abstractVehicle.getName().toLowerCase().startsWith(stringArray[0].toLowerCase())) continue;
                arrayList.add(abstractVehicle.getName());
            }
        }
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (stringArray.length == 2) {
                arrayList.add(player.getLocation().getX() + "");
            }
            if (stringArray.length == 3) {
                arrayList.add(player.getLocation().getY() + "");
            }
            if (stringArray.length == 4) {
                arrayList.add(player.getLocation().getZ() + "");
            }
        }
        return arrayList;
    }
}

