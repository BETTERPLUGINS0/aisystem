package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class CreateArena implements CommandHandlerInterface {
   private TNTRun plugin;

   public CreateArena(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arenac = this.plugin.amanager.getArenaByName(args[0]);
      if (arenac != null) {
         Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c already exists");
         return true;
      } else {
         Arena arena = new Arena(args[0], this.plugin);
         this.plugin.amanager.registerArena(arena);
         Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 created");
         return true;
      }
   }

   public int getMinArgsLength() {
      return 1;
   }
}
