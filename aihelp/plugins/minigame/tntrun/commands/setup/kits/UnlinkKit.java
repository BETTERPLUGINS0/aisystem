package tntrun.commands.setup.kits;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class UnlinkKit implements CommandHandlerInterface {
   private TNTRun plugin;

   public UnlinkKit(TNTRun plugin) {
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
      } else if (!this.plugin.getKitManager().kitExists(args[1]) && !arena.getStructureManager().getLinkedKits().contains(args[1])) {
         Messages.sendMessage(player, Messages.kitnotexists.replace("{KIT}", args[1]));
         return true;
      } else {
         arena.getStructureManager().unlinkKit(args[1]);
         Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 is no longer linked to kit &6" + args[1]);
         return true;
      }
   }

   public int getMinArgsLength() {
      return 2;
   }
}
