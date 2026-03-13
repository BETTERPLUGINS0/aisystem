package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class EnableArena implements CommandHandlerInterface {
   private TNTRun plugin;

   public EnableArena(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStatusManager().isArenaEnabled()) {
            Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c already enabled");
         } else if (!arena.getStructureManager().isArenaFinished()) {
            Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c isn't finished. Please run:&6 /trsetup finish " + args[0]);
         } else if (arena.getStatusManager().enableArena()) {
            Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 enabled");
         } else {
            Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c isn't configured. Reason: &6" + arena.getStructureManager().isArenaConfiguredString());
         }
      } else {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
      }

      return true;
   }

   public int getMinArgsLength() {
      return 1;
   }
}
