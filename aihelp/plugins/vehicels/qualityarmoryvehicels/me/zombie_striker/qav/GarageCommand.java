/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav;

import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.menu.GarageMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GarageCommand
implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] stringArray) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_ONLY_PLAYERs);
            return true;
        }
        Player player = (Player)commandSender;
        if (stringArray.length == 1) {
            if (!commandSender.hasPermission("qualityarmoryvehicles.garage.other")) {
                commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
                return true;
            }
            player = Bukkit.getPlayer((String)stringArray[0]);
        }
        if (player == null) {
            commandSender.sendMessage(Main.prefix + " This player does not exist.");
            return true;
        }
        new GarageMenu((Player)commandSender, player).open();
        return true;
    }
}

