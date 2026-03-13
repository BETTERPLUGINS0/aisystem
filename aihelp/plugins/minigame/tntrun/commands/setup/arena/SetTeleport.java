package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.arena.structure.StructureManager;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class SetTeleport implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetTeleport(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStatusManager().isArenaEnabled()) {
            Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
            return true;
         }

         if (args[1].equalsIgnoreCase("previous")) {
            arena.getStructureManager().setTeleportDestination(StructureManager.TeleportDestination.PREVIOUS);
         } else {
            if (!args[1].equalsIgnoreCase("lobby")) {
               Messages.sendMessage(player, "&c Teleport destination must be &6PREVIOUS &cor &6LOBBY");
               return true;
            }

            if (this.plugin.getGlobalLobby().isLobbyLocationSet()) {
               arena.getStructureManager().setTeleportDestination(StructureManager.TeleportDestination.LOBBY);
            } else {
               Messages.sendMessage(player, "&c Global lobby isn't set, run &6/trsetup setlobby");
            }
         }

         Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 Teleport destination set to &6" + args[1].toUpperCase());
      } else {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
      }

      return true;
   }

   public int getMinArgsLength() {
      return 2;
   }
}
