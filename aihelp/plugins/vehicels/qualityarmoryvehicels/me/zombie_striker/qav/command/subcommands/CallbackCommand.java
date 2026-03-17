/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command.subcommands;

import java.util.ArrayList;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.util.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CallbackCommand
extends SubCommand {
    public CallbackCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "callback";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        return commandSender.hasPermission("qualityarmoryvehicles.callback") ? MessagesConfig.subcommand_callback : null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.callback")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command");
            return;
        }
        Location location = ((Player)commandSender).getLocation();
        int n = 6;
        if (stringArray.length >= 1) {
            n = Integer.parseInt(stringArray[0]);
        }
        for (VehicleEntity vehicleEntity : new ArrayList<VehicleEntity>(Main.vehicles)) {
            if (vehicleEntity.getOwner() == null || !vehicleEntity.getOwner().equals(((Player)commandSender).getUniqueId()) || !(vehicleEntity.getDriverSeat().getLocation().distanceSquared(location) < (double)(n * n))) continue;
            VehicleUtils.callback(vehicleEntity, (Player)commandSender);
        }
        commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGE_CALLBACK.replace("%radius%", String.valueOf(n)));
    }
}

