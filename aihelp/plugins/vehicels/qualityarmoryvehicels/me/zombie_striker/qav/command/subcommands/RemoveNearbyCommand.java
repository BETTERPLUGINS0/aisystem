/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
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
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemoveNearbyCommand
extends SubCommand {
    public RemoveNearbyCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "removeNearbyVehicles";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        return MessagesConfig.subcommand_removeNearbyVehicles;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.removevehicle")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        int n = 6;
        if (stringArray.length > 0) {
            n = Integer.parseInt(stringArray[0]);
        }
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            for (VehicleEntity vehicleEntity : new ArrayList<VehicleEntity>(Main.vehicles)) {
                if (!(vehicleEntity.getDriverSeat().getLocation().distanceSquared(player.getLocation()) < (double)(n * n))) continue;
                vehicleEntity.deconstruct(null, "removeNearbyCommand");
            }
        }
    }

    @Override
    public List<String> complete(CommandSender commandSender, String[] stringArray) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 1; i <= 10; ++i) {
            arrayList.add(String.valueOf(i));
        }
        return arrayList;
    }
}

