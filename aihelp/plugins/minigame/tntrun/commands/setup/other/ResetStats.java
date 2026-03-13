package tntrun.commands.setup.other;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class ResetStats implements CommandHandlerInterface {
   private TNTRun plugin;

   public ResetStats(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      if (!this.plugin.useStats()) {
         Messages.sendMessage(player, Messages.statsdisabled);
         return false;
      } else {
         String uuid = this.plugin.useUuid() ? Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString() : args[0];
         if (!this.plugin.getStats().hasStats(uuid)) {
            Messages.sendMessage(player, "&7 No stats exist for player: &c" + args[0]);
            return false;
         } else {
            this.plugin.getStats().resetStats(uuid);
            Messages.sendMessage(player, "&7 Stats reset for player: &6" + args[0]);
            return true;
         }
      }
   }

   public int getMinArgsLength() {
      return 1;
   }
}
