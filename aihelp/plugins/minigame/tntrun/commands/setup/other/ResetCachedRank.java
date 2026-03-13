package tntrun.commands.setup.other;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class ResetCachedRank implements CommandHandlerInterface {
   private TNTRun plugin;

   public ResetCachedRank(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      if (!this.plugin.getConfig().getBoolean("UseRankInChat.enabled") && this.plugin.getConfig().getBoolean("UseRankInChat.usegroup")) {
         Messages.sendMessage(player, "&7 Caching of ranks is not currently enabled");
         return false;
      } else {
         Utils.removeRankFromCache(args[0]);
         Messages.sendMessage(player, "&7 Cached rank cleared for player: &6" + args[0]);
         return true;
      }
   }

   public int getMinArgsLength() {
      return 1;
   }
}
