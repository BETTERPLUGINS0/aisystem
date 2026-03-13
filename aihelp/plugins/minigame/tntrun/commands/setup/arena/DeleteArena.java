package tntrun.commands.setup.arena;

import java.io.File;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class DeleteArena implements CommandHandlerInterface {
   private TNTRun plugin;

   public DeleteArena(TNTRun plugin) {
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
      } else {
         String var10002 = String.valueOf(this.plugin.getDataFolder());
         (new File(var10002 + File.separator + "arenas" + File.separator + arena.getArenaName() + ".yml")).delete();
         this.plugin.getSignEditor().removeArena(arena.getArenaName());
         this.plugin.amanager.unregisterArena(arena);
         Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 deleted");
         return true;
      }
   }

   public int getMinArgsLength() {
      return 1;
   }
}
