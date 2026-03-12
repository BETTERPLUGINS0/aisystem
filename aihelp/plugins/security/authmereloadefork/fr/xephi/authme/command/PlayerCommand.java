package fr.xephi.authme.command;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PlayerCommand implements ExecutableCommand {
   public void executeCommand(CommandSender sender, List<String> arguments) {
      if (sender instanceof Player) {
         this.runCommand((Player)sender, arguments);
      } else {
         String alternative = this.getAlternativeCommand();
         if (alternative != null) {
            sender.sendMessage("Player only! Please use " + alternative + " instead.");
         } else {
            sender.sendMessage("This command is only for players.");
         }
      }

   }

   protected abstract void runCommand(Player var1, List<String> var2);

   protected String getAlternativeCommand() {
      return null;
   }
}
