/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command.subcommands;

import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReloadCommand
extends SubCommand {
    public ReloadCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        return commandSender.hasPermission("qualityarmoryvehicles.reload") ? " : Reloads the plugin" : null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.reload")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        long l = System.currentTimeMillis();
        QualityArmoryVehicles.getPlugin().initVals();
        Main.loadVehicles(true);
        commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_RELOAD.replace("%time%", String.valueOf(System.currentTimeMillis() - l)));
    }
}

