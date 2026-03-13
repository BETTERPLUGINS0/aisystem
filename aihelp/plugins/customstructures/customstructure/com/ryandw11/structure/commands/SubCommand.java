package com.ryandw11.structure.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface SubCommand {
   boolean subCommand(CommandSender var1, Command var2, String var3, String[] var4);
}
