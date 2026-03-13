package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class SetMinPlayers implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetMinPlayers(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStatusManager().isArenaEnabled()) {
            Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
            return true;
         }

         if (!Utils.isNumber(args[1]) || Integer.parseInt(args[1]) <= 0) {
            Messages.sendMessage(player, "&c MinPlayers must be a positive integer");
            return true;
         }

         if (Integer.parseInt(args[1]) <= arena.getStructureManager().getMaxPlayers()) {
            arena.getStructureManager().setMinPlayers(Integer.parseInt(args[1]));
            Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 MinPlayers set to &6" + args[1]);
         } else {
            Messages.sendMessage(player, "&c MinPlayers cannot be greater than MaxPlayers");
         }
      } else {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
      }

      return true;
   }

   public int getMinArgsLength() {
      return 2;
   }
}
