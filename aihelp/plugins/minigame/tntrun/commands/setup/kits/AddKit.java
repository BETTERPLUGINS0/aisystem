package tntrun.commands.setup.kits;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;

public class AddKit implements CommandHandlerInterface {
   private TNTRun plugin;

   public AddKit(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      this.plugin.getKitManager().registerKit(args[0], player);
      return true;
   }

   public int getMinArgsLength() {
      return 1;
   }
}
