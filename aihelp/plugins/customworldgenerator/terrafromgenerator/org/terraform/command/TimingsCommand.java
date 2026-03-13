package org.terraform.command;

import java.util.Iterator;
import java.util.Stack;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.terraform.command.contants.TerraCommand;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.TickTimer;

public class TimingsCommand extends TerraCommand {
   public TimingsCommand(TerraformGeneratorPlugin plugin, String... aliases) {
      super(plugin, aliases);
   }

   @NotNull
   public String getDefaultDescription() {
      return "Shows timings of monitored functions";
   }

   public boolean canConsoleExec() {
      return true;
   }

   public boolean hasPermission(@NotNull CommandSender sender) {
      return sender.isOp();
   }

   public void execute(@NotNull CommandSender sender, Stack<String> args) {
      sender.sendMessage("=====Avg Timings=====");
      Iterator var3 = TickTimer.TIMINGS.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Long> entry = (Entry)var3.next();
         String var10001 = String.valueOf(ChatColor.GRAY);
         sender.sendMessage(var10001 + "- " + String.valueOf(ChatColor.GREEN) + (String)entry.getKey() + String.valueOf(ChatColor.DARK_GRAY) + ": " + String.valueOf(ChatColor.GOLD) + String.valueOf(entry.getValue()));
      }

   }
}
