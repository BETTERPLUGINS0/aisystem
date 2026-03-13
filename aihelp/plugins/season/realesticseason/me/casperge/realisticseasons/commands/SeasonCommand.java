package me.casperge.realisticseasons.commands;

import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.data.LanguageManager;
import me.casperge.realisticseasons.data.MessageType;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SeasonCommand implements CommandExecutor {
   public RealisticSeasons main;

   public SeasonCommand(RealisticSeasons var1) {
      this.main = var1;
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      if (var1 instanceof Player) {
         Player var5 = (Player)var1;
         if (var5.hasPermission("realisticseasons.getinfo")) {
            this.main.getSeasonManager().sendSeasonInfo(var5);
         } else {
            var5.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.NO_PERMISSION)));
         }
      } else if (var1 instanceof ConsoleCommandSender) {
         if (var4.length != 1) {
            Bukkit.getLogger().info("Usage: /season <world>");
         } else {
            World var6 = Bukkit.getWorld(var4[0]);
            if (var6 == null) {
               Bukkit.getLogger().info("ERROR: Invalid world");
               return true;
            }

            this.main.getSeasonManager().sendSeasonInfoToConsole(var6);
         }
      }

      return true;
   }
}
