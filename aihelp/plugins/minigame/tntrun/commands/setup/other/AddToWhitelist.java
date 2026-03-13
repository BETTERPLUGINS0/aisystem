package tntrun.commands.setup.other;

import java.util.List;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class AddToWhitelist implements CommandHandlerInterface {
   private TNTRun plugin;

   public AddToWhitelist(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      List<String> whitelist = this.plugin.getConfig().getStringList("commandwhitelist");
      String cmd = String.join(" ", args);
      if (whitelist.contains(cmd)) {
         Messages.sendMessage(player, "&7 Command is already whitelisted: &6" + cmd);
         return true;
      } else {
         whitelist.add(cmd);
         this.plugin.getConfig().set("commandwhitelist", whitelist);
         this.plugin.saveConfig();
         Messages.sendMessage(player, "&7 Command added to whitelist: &6" + cmd);
         return true;
      }
   }

   public int getMinArgsLength() {
      return 1;
   }
}
