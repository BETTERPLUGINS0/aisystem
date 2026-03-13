package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class SetMoneyRewards implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetMoneyRewards(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStatusManager().isArenaEnabled()) {
            Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
            return true;
         }

         if (Utils.isNumber(args[1])) {
            arena.getStructureManager().getRewards().setMoneyReward(Integer.parseInt(args[1]), 1);
            Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 Money reward set to &6" + Utils.getFormattedCurrency(args[1]));
         } else {
            Messages.sendMessage(player, "&c The reward amount must be an integer");
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
