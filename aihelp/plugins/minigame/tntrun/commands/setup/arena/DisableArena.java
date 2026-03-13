package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class DisableArena implements CommandHandlerInterface {
   private TNTRun plugin;

   public DisableArena(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStatusManager().isArenaEnabled()) {
            arena.getStatusManager().disableArena();
            Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 disabled");
         } else {
            Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c already disabled");
         }
      } else {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
      }

      return true;
   }

   public int getMinArgsLength() {
      return 1;
   }
}
