package com.volmit.iris.util.plugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RouterCommand extends org.bukkit.command.Command {
   private final CommandExecutor ex;
   private String usage;

   public RouterCommand(ICommand realCommand, CommandExecutor ex) {
      super(var1.getNode().toLowerCase());
      this.setAliases(var1.getNodes());
      this.ex = var2;
   }

   public org.bukkit.command.Command setUsage(String u) {
      this.usage = var1;
      return this;
   }

   public String getUsage() {
      return this.usage;
   }

   public boolean execute(CommandSender sender, String commandLabel, String[] args) {
      return this.ex.onCommand(var1, this, var2, var3);
   }
}
