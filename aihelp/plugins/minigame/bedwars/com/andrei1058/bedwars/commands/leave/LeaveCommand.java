/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.command.defaults.BukkitCommand
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.leave;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class LeaveCommand
extends BukkitCommand {
    public LeaveCommand(String name) {
        super(name);
    }

    public boolean execute(CommandSender s, String st, String[] args) {
        if (s instanceof ConsoleCommandSender) {
            return true;
        }
        Player p = (Player)s;
        Bukkit.dispatchCommand((CommandSender)p, (String)"bw leave");
        return true;
    }
}

