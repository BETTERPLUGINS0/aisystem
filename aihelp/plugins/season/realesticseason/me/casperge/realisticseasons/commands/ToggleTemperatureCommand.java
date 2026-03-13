package me.casperge.realisticseasons.commands;

import java.util.Iterator;
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
import org.bukkit.potion.PotionEffectType;

public class ToggleTemperatureCommand implements CommandExecutor {
   public RealisticSeasons main;

   public ToggleTemperatureCommand(RealisticSeasons var1) {
      this.main = var1;
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      Player var5;
      Iterator var6;
      PotionEffectType var7;
      if (var1 instanceof Player) {
         var5 = (Player)var1;
         if (var4.length >= 1 && var5.hasPermission("realisticseasons.toggletemperature.others")) {
            Player var9 = Bukkit.getPlayer(var4[0]);
            if (var9 == null) {
               var5.sendMessage(ChatColor.RED + "ERROR: Could not find that player");
               return true;
            }

            if (!this.main.getTemperatureManager().hasTemperature(var9)) {
               this.main.getTemperatureManager().enableTemperature(var9);
               var5.sendMessage(ChatColor.GREEN + "Enabled temperature for " + var9.getName());
            } else {
               this.main.getTemperatureManager().disableTemperature(var9);
               Iterator var10 = this.main.getTemperatureManager().getTempData().getTempSettings().getBoostPotionEffects().iterator();

               while(var10.hasNext()) {
                  PotionEffectType var8 = (PotionEffectType)var10.next();
                  if (var9.hasPotionEffect(var8)) {
                     var9.removePotionEffect(var8);
                  }
               }

               var5.sendMessage(ChatColor.GREEN + "Disabled temperature for " + var9.getName());
            }
         } else if (var5.hasPermission("realisticseasons.toggletemperature")) {
            if (!this.main.getTemperatureManager().hasTemperature(var5)) {
               this.main.getTemperatureManager().enableTemperature(var5);
               var5.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.TOGGLETEMPERATURECOMMAND)).replaceAll("\\$status\\$", (String)LanguageManager.messages.get(MessageType.ENABLED))));
            } else {
               this.main.getTemperatureManager().disableTemperature(var5);
               var6 = this.main.getTemperatureManager().getTempData().getTempSettings().getBoostPotionEffects().iterator();

               while(var6.hasNext()) {
                  var7 = (PotionEffectType)var6.next();
                  if (var5.hasPotionEffect(var7)) {
                     var5.removePotionEffect(var7);
                  }
               }

               var5.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.TOGGLETEMPERATURECOMMAND)).replaceAll("\\$status\\$", (String)LanguageManager.messages.get(MessageType.DISABLED))));
            }
         } else {
            var5.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.NO_PERMISSION)));
         }
      } else if (var1 instanceof ConsoleCommandSender) {
         if (var4.length < 1) {
            Bukkit.getLogger().info("Usage: /toggletemperature <player>");
         } else {
            var5 = Bukkit.getPlayer(var4[0]);
            if (var5 == null) {
               Bukkit.getLogger().info("ERROR: player not found");
            } else if (!this.main.getTemperatureManager().hasTemperature(var5)) {
               this.main.getTemperatureManager().enableTemperature(var5);
               Bukkit.getLogger().info("Enabled temperature for " + var5.getName());
            } else {
               this.main.getTemperatureManager().disableTemperature(var5);
               Bukkit.getLogger().info("Disabled temperature for " + var5.getName());
               var6 = this.main.getTemperatureManager().getTempData().getTempSettings().getBoostPotionEffects().iterator();

               while(var6.hasNext()) {
                  var7 = (PotionEffectType)var6.next();
                  if (var5.hasPotionEffect(var7)) {
                     var5.removePotionEffect(var7);
                  }
               }
            }
         }
      }

      return true;
   }
}
