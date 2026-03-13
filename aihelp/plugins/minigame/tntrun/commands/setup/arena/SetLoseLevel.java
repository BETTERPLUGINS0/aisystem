package tntrun.commands.setup.arena;

import java.text.DecimalFormat;
import java.util.StringJoiner;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class SetLoseLevel implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetLoseLevel(TNTRun plugin) {
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
      } else if (arena.getStructureManager().getWorldName() == null) {
         Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c bounds are wrong");
         return true;
      } else {
         Location loc = player.getLocation();
         if (arena.getStructureManager().setLoseLevel(loc)) {
            Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 loselevel set:\n&6Y = " + loc.getY());
         } else {
            Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c error: loselevel (&6" + this.displayLocation(loc) + "&c) is not within the bounds of the arena:\nP1 = &6" + String.valueOf(arena.getStructureManager().getP1()) + "\n&cP2 = &6" + String.valueOf(arena.getStructureManager().getP2()));
         }

         return true;
      }
   }

   private String displayLocation(Location loc) {
      StringJoiner msg = new StringJoiner(", ");
      DecimalFormat df = new DecimalFormat("#.#");
      msg.add(df.format(loc.getX())).add(df.format(loc.getY())).add(df.format(loc.getZ()));
      return msg.toString();
   }

   public int getMinArgsLength() {
      return 1;
   }
}
