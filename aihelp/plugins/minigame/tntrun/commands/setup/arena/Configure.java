package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class Configure implements CommandHandlerInterface {
   private TNTRun plugin;

   public Configure(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         this.plugin.getMenus().buildConfigMenu(player, arena, 1);
         return true;
      } else {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
         return false;
      }
   }

   public int getMinArgsLength() {
      return 1;
   }
}
