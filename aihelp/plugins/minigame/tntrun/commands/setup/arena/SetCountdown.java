package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class SetCountdown implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetCountdown(TNTRun plugin) {
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
            Messages.sendMessage(player, "&c Countdown must be a positive integer");
            return true;
         }

         arena.getStructureManager().setCountdown(Integer.parseInt(args[1]));
         Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 Countdown set to &6" + args[1] + "&7 seconds");
      } else {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
      }

      return true;
   }

   public int getMinArgsLength() {
      return 2;
   }
}
