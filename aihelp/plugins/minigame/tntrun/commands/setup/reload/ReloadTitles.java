package tntrun.commands.setup.reload;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.utils.TitleMsg;

public class ReloadTitles implements CommandHandlerInterface {
   private TNTRun plugin;

   public ReloadTitles(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      TitleMsg.loadTitles(this.plugin);
      player.sendMessage("Titles reloaded");
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
