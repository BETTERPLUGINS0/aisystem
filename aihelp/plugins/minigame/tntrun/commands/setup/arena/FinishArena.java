package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.utils.Bars;
import tntrun.utils.Utils;

public class FinishArena implements CommandHandlerInterface {
   private TNTRun plugin;

   public FinishArena(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (!arena.getStatusManager().isArenaEnabled()) {
            if (arena.getStructureManager().isArenaConfigured()) {
               arena.getStructureManager().setArenaFinished(true);
               arena.getStructureManager().saveToConfig();
               arena.getStatusManager().enableArena();
               Bars.createBar(args[0]);
               Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 saved and enabled");
               if (Utils.debug()) {
                  this.plugin.getLogger().info("Arena " + args[0] + " finished successfully");
               }
            } else {
               Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c isn't configured. Reason: &6" + arena.getStructureManager().isArenaConfiguredString());
               if (Utils.debug()) {
                  this.plugin.getLogger().info("Arena " + args[0] + " finish failed: " + arena.getStructureManager().isArenaConfiguredString());
               }
            }
         } else {
            Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
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
