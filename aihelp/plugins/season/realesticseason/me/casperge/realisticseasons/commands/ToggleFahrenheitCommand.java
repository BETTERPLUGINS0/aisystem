package me.casperge.realisticseasons.commands;

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

public class ToggleFahrenheitCommand implements CommandExecutor {
   public RealisticSeasons main;

   public ToggleFahrenheitCommand(RealisticSeasons var1) {
      this.main = var1;
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      if (var1 instanceof Player) {
         Player var5 = (Player)var1;
         if (var5.hasPermission("realisticseasons.togglefahrenheit")) {
            if (this.main.getTemperatureManager().hasFahrenheitEnabled(var5)) {
               this.main.getTemperatureManager().toggleFahrenheit(var5);
               if (this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit()) {
                  var5.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.FAHRENHEITCOMMAND_TOFAHRENHEIT)));
               } else {
                  var5.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.FAHRENHEITCOMMAND_TOCELCIUS)));
               }
            } else {
               this.main.getTemperatureManager().toggleFahrenheit(var5);
               if (this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit()) {
                  var5.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.FAHRENHEITCOMMAND_TOCELCIUS)));
               } else {
                  var5.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.FAHRENHEITCOMMAND_TOFAHRENHEIT)));
               }
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
