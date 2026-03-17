/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command.subcommands;

import java.util.ArrayList;
import java.util.UUID;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.util.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CallbackAllCommand
extends SubCommand {
    public CallbackAllCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "callbackAll";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        return commandSender.hasPermission("qualityarmoryvehicles.callbackAll") ? MessagesConfig.subcommand_callbackAll : null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.callbackAll")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        int n = 0;
        for (VehicleEntity vehicleEntity : new ArrayList<VehicleEntity>(Main.vehicles)) {
            if (vehicleEntity.getOwner() == null || Bukkit.getPlayer((UUID)vehicleEntity.getOwner()) == null) continue;
            VehicleUtils.callback(vehicleEntity, Bukkit.getPlayer((UUID)vehicleEntity.getOwner()));
            ++n;
        }
        commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGE_CALLBACKALL.replace("%count%", String.valueOf(n)));
    }
}

