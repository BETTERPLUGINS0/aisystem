package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class DeleteSpawnPoints implements CommandHandlerInterface {
   private TNTRun plugin;

   public DeleteSpawnPoints(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena == null) {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
         return true;
      } else if (arena.getStatusManager().isArenaEnabled()) {
         Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
         return true;
      } else {
         arena.getStructureManager().removeAdditionalSpawnPoints();
         Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 all additional spawn points have been deleted");
         return true;
      }
   }

   public int getMinArgsLength() {
      return 1;
   }
}
