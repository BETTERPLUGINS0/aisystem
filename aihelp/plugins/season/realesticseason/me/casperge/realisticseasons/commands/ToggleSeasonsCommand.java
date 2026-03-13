package me.casperge.realisticseasons.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.data.LanguageManager;
import me.casperge.realisticseasons.data.MessageType;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ToggleSeasonsCommand implements CommandExecutor {
   public RealisticSeasons main;
   public static List<UUID> disabled = new ArrayList();

   public ToggleSeasonsCommand(RealisticSeasons var1) {
      this.main = var1;
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      if (var1 instanceof Player) {
         Player var5 = (Player)var1;
         if (var5.hasPermission("realisticseasons.toggleseasons")) {
            if (disabled.contains(var5.getUniqueId())) {
               disabled.remove(var5.getUniqueId());
               var5.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.TOGGLESEASONCOMMAND_MESSAGE)).replaceAll("\\$status\\$", (String)LanguageManager.messages.get(MessageType.ENABLED))));
            } else {
               disabled.add(var5.getUniqueId());
               var5.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.TOGGLESEASONCOMMAND_MESSAGE)).replaceAll("\\$status\\$", (String)LanguageManager.messages.get(MessageType.DISABLED))));
            }
         } else {
            var5.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.NO_PERMISSION)));
         }
      } else if (var1 instanceof ConsoleCommandSender) {
         Bukkit.getLogger().log(Level.WARNING, "You cant use this command as the console!");
      }

      return true;
   }
}
