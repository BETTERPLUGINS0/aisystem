/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.command.subcommands;

import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ListCommand
extends SubCommand {
    public ListCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription(@NotNull CommandSender commandSender) {
        return MessagesConfig.subcommand_list;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (AbstractVehicle abstractVehicle : Main.vehicleTypes) {
            stringBuilder.append(abstractVehicle.getName()).append(", ");
        }
        commandSender.sendMessage(Main.prefix + " Loaded vehicles: " + stringBuilder.substring(0, stringBuilder.length() - 2));
    }
}

