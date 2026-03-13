package tntrun.commands.setup.arena;

import java.util.logging.Logger;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.selectionget.PlayerCuboidSelection;
import tntrun.selectionget.PlayerSelection;
import tntrun.utils.Utils;

public class SetArena implements CommandHandlerInterface {
   private TNTRun plugin;
   private PlayerSelection selection;

   public SetArena(TNTRun plugin, PlayerSelection selection) {
      this.plugin = plugin;
      this.selection = selection;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStatusManager().isArenaEnabled()) {
            Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
            return true;
         }

         PlayerCuboidSelection sel = this.selection.getPlayerSelection(player);
         if (sel != null) {
            arena.getStructureManager().setArenaPoints(sel.getMinimumLocation(), sel.getMaximumLocation());
            Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 set");
            if (Utils.debug()) {
               Logger var10000 = this.plugin.getLogger();
               String var10001 = arena.getArenaName();
               var10000.info("Arena " + var10001 + " min point: " + sel.getMinimumLocation().toVector().toString());
               var10000 = this.plugin.getLogger();
               var10001 = arena.getArenaName();
               var10000.info("Arena " + var10001 + " max point: " + sel.getMaximumLocation().toVector().toString());
            }
         } else {
            Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c locations are wrong - retry or use WorldEdit to select the arena bounds");
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
