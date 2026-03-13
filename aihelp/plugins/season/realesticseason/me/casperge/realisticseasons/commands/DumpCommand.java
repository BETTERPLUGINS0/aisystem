package me.casperge.realisticseasons.commands;

import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.data.LanguageManager;
import me.casperge.realisticseasons.data.MessageType;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class DumpCommand implements CommandExecutor {
   public RealisticSeasons main;
   public static boolean needsAgreeing = false;
   public static String key;

   public DumpCommand(RealisticSeasons var1) {
      this.main = var1;
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      if (var1 instanceof Player) {
         Player var5 = (Player)var1;
         if (var5.hasPermission("realisticseasons.admin")) {
            if (var4.length != 1) {
               var5.sendMessage(ChatColor.RED + "Usage: /seasondump <key>");
               var5.sendMessage(ChatColor.RED + "Command should only be used when asked by support and requires a one-time use key.");
            } else if (!needsAgreeing) {
               needsAgreeing = true;
               key = var4[0];
               var5.sendMessage(ChatColor.GREEN + "This command uploads some server data to the RealisticSeasons support server, including your server log and RealisticSeasons configuration files.");
               var5.sendMessage(ChatColor.GREEN + "Your data will only be used for debugging and answering your support messages. More information can be found here: " + ChatColor.DARK_GREEN + "paste.realisticseasons.com/data.html");
               var5.sendMessage(ChatColor.GREEN + "To cancel the upload, type " + ChatColor.DARK_GREEN + "/seasondump cancel");
               var5.sendMessage(ChatColor.GREEN + "If you agree to RealisticSeasons using your data as listed on the page, type " + ChatColor.DARK_GREEN + "/seasondump confirm");
            } else if (var4[0].equalsIgnoreCase("confirm")) {
               needsAgreeing = false;
               this.main.createDump(var5.getUniqueId(), key);
            } else if (var4[0].equalsIgnoreCase("cancel")) {
               needsAgreeing = false;
               var5.sendMessage(ChatColor.RED + "Successfully cancelled dump upload.");
            } else {
               var5.sendMessage(ChatColor.RED + "Please type " + ChatColor.DARK_RED + "/seasondump confirm" + ChatColor.RED + " or " + ChatColor.DARK_RED + "/seasondump cancel");
            }
         } else {
            var5.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.NO_PERMISSION)));
         }
      } else if (var1 instanceof ConsoleCommandSender) {
         if (var4.length != 1) {
            Bukkit.getLogger().info("[RealisticSeasons] Usage: 'seasondump <key>'");
            Bukkit.getLogger().info("[RealisticSeasons] Command should only be used when asked by support and requires a one-time use key.");
         } else if (!needsAgreeing) {
            needsAgreeing = true;
            key = var4[0];
            Bukkit.getLogger().info("[RealisticSeasons] This command uploads some server data to the RealisticSeasons support server, including your server log and RealisticSeasons configuration files.");
            Bukkit.getLogger().info("[RealisticSeasons] Your data will only be used for debugging and answering your support messages. More information can be found here: paste.realisticseasons.com/data.html");
            Bukkit.getLogger().info("[RealisticSeasons] To cancel the upload, type 'seasondump cancel'");
            Bukkit.getLogger().info("[RealisticSeasons] If you agree to RealisticSeasons using your data as listed on the page, type 'seasondump confirm'");
         } else if (var4[0].equalsIgnoreCase("confirm")) {
            needsAgreeing = false;
            this.main.createConsoleDump(key);
         } else if (var4[0].equalsIgnoreCase("cancel")) {
            needsAgreeing = false;
            Bukkit.getLogger().info("[RealisticSeasons] Successfully cancelled dump upload.");
         } else {
            Bukkit.getLogger().info("[RealisticSeasons] Please type 'seasondump confirm' or 'seasondump cancel'");
         }
      }

      return true;
   }
}
