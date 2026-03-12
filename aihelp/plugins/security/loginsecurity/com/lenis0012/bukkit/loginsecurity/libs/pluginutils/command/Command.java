package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.command;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command implements CommandExecutor {
   private String usage = null;
   private String permission = null;
   private boolean allowConsole = true;
   private int minArgs = 0;
   private String prefix = "";
   private ChatColor successColor;
   private ChatColor errorColor;
   protected CommandSender sender;
   protected Player player;
   protected boolean isPlayer;
   private String[] args;

   public Command() {
      this.successColor = ChatColor.GREEN;
      this.errorColor = ChatColor.RED;
   }

   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
      this.isPlayer = sender instanceof Player;
      this.sender = sender;
      this.args = args;
      if (!this.allowConsole && !this.isPlayer) {
         this.reply(false, "You must be a player to execute this command!");
         return true;
      } else {
         if (this.isPlayer) {
            this.player = (Player)sender;
         }

         if (this.permission != null && !sender.hasPermission(this.permission)) {
            this.reply(false, "You don't have permission to execute this command!");
            return true;
         } else if (args.length < this.minArgs) {
            this.reply(false, "This command needs at least %s arguments!", this.minArgs);
            return true;
         } else {
            try {
               this.execute();
            } catch (Exception var6) {
               this.reply(false, "An error occured while executing this command, please contact an admin!");
               Bukkit.getLogger().log(Level.SEVERE, "Error while running command", var6);
            }

            return true;
         }
      }
   }

   public abstract void execute();

   protected void reply(String message, Object... args) {
      this.reply(true, message, args);
   }

   protected void reply(boolean success, Object message, Object... args) {
      this.reply(this.sender, success, message, args);
   }

   protected void reply(CommandSender sender, boolean success, Object message, Object... args) {
      String text = this.prefix + (success ? this.successColor : this.errorColor).toString() + ChatColor.translateAlternateColorCodes('&', String.format(message.toString(), args));
      sender.sendMessage(text);
   }

   protected String getArg(int index) {
      return this.args[index];
   }

   protected int getArgAsInt(int index) {
      return Integer.parseInt(this.getArg(index));
   }

   protected Player getArgAsPlayer(int index) {
      return Bukkit.getPlayer(this.getArg(index));
   }

   protected int getArgLength() {
      return this.args.length;
   }

   protected String getUsage() {
      return this.usage;
   }

   protected void setUsage(String usage) {
      this.usage = usage;
   }

   protected String getPermission() {
      return this.permission;
   }

   protected void setPermission(String permission) {
      this.permission = permission;
   }

   protected int getMinArgs() {
      return this.minArgs;
   }

   protected void setMinArgs(int minArgs) {
      this.minArgs = minArgs;
   }

   protected boolean isAllowConsole() {
      return this.allowConsole;
   }

   protected void setAllowConsole(boolean allowConsole) {
      this.allowConsole = allowConsole;
   }

   protected String getPrefix() {
      return this.prefix;
   }

   protected void setPrefix(String prefix) {
      this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
   }

   protected ChatColor getSuccessColor() {
      return this.successColor;
   }

   protected void setSuccessColor(ChatColor successColor) {
      this.successColor = successColor;
   }

   protected ChatColor getErrorColor() {
      return this.errorColor;
   }

   protected void setErrorColor(ChatColor errorColor) {
      this.errorColor = errorColor;
   }
}
