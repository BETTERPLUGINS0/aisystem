package tntrun.commands.setup.reload;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.utils.Bars;

public class ReloadBars implements CommandHandlerInterface {
   private TNTRun plugin;

   public ReloadBars(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Bars.loadBars(this.plugin);
      player.sendMessage("Bars reloaded");
      return true;
   }

   public int getMinArgsLength() {
      return 0;
   }
}
