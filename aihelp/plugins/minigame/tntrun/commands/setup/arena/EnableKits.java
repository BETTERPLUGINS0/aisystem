package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class EnableKits implements CommandHandlerInterface {
   private TNTRun plugin;

   public EnableKits(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStructureManager().isKitsEnabled()) {
            Messages.sendMessage(player, "&c Kits are already enabled for arena &6" + args[0]);
            return true;
         }

         if (arena.getStatusManager().isArenaEnabled()) {
            Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
            return true;
         }

         arena.getStructureManager().enableKits(true);
         Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 Kits have been &6enabled");
      } else {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
      }

      return true;
   }

   public int getMinArgsLength() {
      return 1;
   }
}
