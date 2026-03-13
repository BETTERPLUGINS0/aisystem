package me.casperge.realisticseasons.commands;

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

public class BiomeCommand implements CommandExecutor {
   public RealisticSeasons main;

   public BiomeCommand(RealisticSeasons var1) {
      this.main = var1;
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      if (var1 instanceof Player) {
         Player var5 = (Player)var1;
         if (var5.hasPermission("realisticseasons.getbiome")) {
            var5.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.CURRENTBIOME_MESSAGE)).replaceAll("\\$biome\\$", this.main.getNMSUtils().getBiomeName(((Player)var1).getLocation()))));
         } else {
            var5.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.NO_PERMISSION)));
         }
      } else if (var1 instanceof ConsoleCommandSender) {
         Bukkit.getLogger().info("ERROR: Command can not be used as console");
      }

      return true;
   }
}
