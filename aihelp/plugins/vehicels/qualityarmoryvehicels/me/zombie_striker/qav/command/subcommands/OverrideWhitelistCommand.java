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

import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.util.VehicleUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OverrideWhitelistCommand
extends SubCommand {
    public OverrideWhitelistCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "overrideWhitelist";
    }

    @Override
    @Nullable
    public String getDescription(@NotNull CommandSender commandSender) {
        if (commandSender.hasPermission("qualityarmoryvehicles.overrideWhitelistCommand")) {
            return " : Toggles the override whitelist";
        }
        return null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.overrideWhitelistCommand")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_ONLY_PLAYERs);
            return;
        }
        VehicleUtils.toggleOverrideWhitelisted(((Player)commandSender).getUniqueId());
        commandSender.sendMessage(MessagesConfig.COMMANDMESSAGES_WHITELIST_OVERRIDE);
    }
}

