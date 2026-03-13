package tntrun.commands.setup.arena;

import java.util.logging.Logger;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class SetSpawn implements CommandHandlerInterface {
   private TNTRun plugin;

   public SetSpawn(TNTRun plugin) {
      this.plugin = plugin;
   }

   public boolean handleCommand(Player player, String[] args) {
      Arena arena = this.plugin.amanager.getArenaByName(args[0]);
      if (arena != null) {
         if (arena.getStatusManager().isArenaEnabled()) {
            Messages.sendMessage(player, Messages.arenanotdisabled.replace("{ARENA}", args[0]));
            return true;
         }

         if (!arena.getStructureManager().isArenaBoundsSet()) {
            Messages.sendMessage(player, Messages.arenanobounds);
            return true;
         }

         if (arena.getStructureManager().setSpawnPoint(player.getLocation())) {
            Messages.sendMessage(player, "&7 Arena &6" + args[0] + "&7 spawn point set to &6X: &7" + Math.round(player.getLocation().getX()) + " &6Y: &7" + Math.round(player.getLocation().getY()) + " &6Z: &7" + Math.round(player.getLocation().getZ()));
         } else {
            Messages.sendMessage(player, "&c Arena &6" + args[0] + "&c spawn point must be in arena bounds");
         }

         if (Utils.debug()) {
            Logger var10000 = this.plugin.getLogger();
            String var10001 = arena.getArenaName();
            var10000.info("Arena " + var10001 + " spawnpoint: " + player.getLocation().toVector().toString());
            this.plugin.getLogger().info("Min point: " + arena.getStructureManager().getP1().toString());
            this.plugin.getLogger().info("Max point: " + arena.getStructureManager().getP2().toString());
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
