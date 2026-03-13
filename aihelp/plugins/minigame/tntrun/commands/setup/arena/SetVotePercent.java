package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class SetVotePercent implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetVotePercent(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStatusManager().isArenaEnabled()) {
            Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
            return true;
         }

         if (Utils.isDouble(args[1]) && Double.valueOf(args[1]) > 0.0D && Double.valueOf(args[1]) < 1.0D) {
            arena.getStructureManager().setVotePercent(Double.valueOf(args[1]));
            Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 VotePercent set to &6" + String.valueOf(Double.valueOf(args[1])));
         } else {
            Messages.sendMessage(player, "&c VotePercent amount must be an decimal between 0 and 1");
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
