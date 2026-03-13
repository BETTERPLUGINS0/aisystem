package me.casperge.realisticseasons.commands;

import java.util.logging.Level;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.data.LanguageManager;
import me.casperge.realisticseasons.data.MessageType;
import me.casperge.realisticseasons.particle.ParticleManager;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ToggleParticleCommand implements CommandExecutor {
   public RealisticSeasons main;

   public ToggleParticleCommand(RealisticSeasons var1) {
      this.main = var1;
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      if (var1 instanceof Player) {
         Player var5 = (Player)var1;
         if (var5.hasPermission("realisticseasons.toggleparticles")) {
            if (ParticleManager.disabledParticles.contains(var5.getUniqueId())) {
               ParticleManager.disabledParticles.remove(var5.getUniqueId());
               var5.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.TOGGLEPARTICLESCOMMAND)).replaceAll("\\$status\\$", (String)LanguageManager.messages.get(MessageType.ENABLED))));
            } else {
               ParticleManager.disabledParticles.add(var5.getUniqueId());
               var5.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.TOGGLEPARTICLESCOMMAND)).replaceAll("\\$status\\$", (String)LanguageManager.messages.get(MessageType.DISABLED))));
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
