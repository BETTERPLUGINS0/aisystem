package tntrun.commands.setup.reload;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class ReloadMSG implements CommandHandlerInterface {
   private TNTRun plugin;

   public ReloadMSG(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      this.plugin.reloadConfig();
      Messages.loadMessages(this.plugin);
      player.sendMessage("Messages reloaded");
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
