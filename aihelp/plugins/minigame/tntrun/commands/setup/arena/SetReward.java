package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.conversation.ConversationType;
import tntrun.conversation.TNTRunConversation;
import tntrun.messages.Messages;

public class SetReward implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetReward(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStatusManager().isArenaEnabled()) {
            Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
            return true;
         }

         (new TNTRunConversation(this.plugin, player, arena, ConversationType.ARENAPRIZE)).begin();
         Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 set reward");
      } else {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
      }

      return true;
   }

   public int getMinArgsLength() {
      return 1;
   }
}
