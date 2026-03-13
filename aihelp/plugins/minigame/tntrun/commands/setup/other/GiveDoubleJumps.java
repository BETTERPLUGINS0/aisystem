package tntrun.commands.setup.other;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class GiveDoubleJumps implements CommandHandlerInterface {
   private TNTRun plugin;

   public GiveDoubleJumps(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getPlayerArena(args[0]);
      if (arena == null) {
         Messages.sendMessage(player, "&7 " + args[0] + "&c is not in a TNTRun arena");
         return false;
      } else if (Utils.isNumber(args[1]) && Integer.parseInt(args[1]) > 0) {
         arena.getPlayerHandler().incrementDoubleJumps(args[0], Integer.parseInt(args[1]));
         Messages.sendMessage(player, "&7 " + args[1] + " doublejumps given to: &6" + args[0]);
         if (!arena.getStatusManager().isArenaStarting() && this.plugin.getConfig().getBoolean("scoreboard.displaydoublejumps") && this.plugin.getConfig().getBoolean("special.UseScoreboard")) {
            arena.getScoreboardHandler().updateWaitingScoreboard(Bukkit.getPlayer(args[0]));
         }

         return true;
      } else {
         Messages.sendMessage(player, "&c DoubleJumps must be a positive integer");
         return false;
      }
   }

   public int getMinArgsLength() {
      return 2;
   }
}
