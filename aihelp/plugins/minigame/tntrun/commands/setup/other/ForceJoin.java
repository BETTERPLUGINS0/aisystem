package tntrun.commands.setup.other;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class ForceJoin implements CommandHandlerInterface {
   private TNTRun plugin;

   public ForceJoin(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena == null) {
         Messages.sendMessage(player, Messages.arenanotexist.replace("{ARENA}", args[0]));
         return false;
      } else {
         Iterator var4 = Bukkit.getOnlinePlayers().iterator();

         while(var4.hasNext()) {
            Player onlinePlayer = (Player)var4.next();
            if (!onlinePlayer.hasPermission("tntrun.forcejoinbypass") && arena.getPlayerHandler().checkJoin(onlinePlayer)) {
               arena.getPlayerHandler().spawnPlayer(onlinePlayer, Messages.playerjoinedtoothers);
            }
         }

         return true;
      }
   }

   public int getMinArgsLength() {
      return 1;
   }
}
