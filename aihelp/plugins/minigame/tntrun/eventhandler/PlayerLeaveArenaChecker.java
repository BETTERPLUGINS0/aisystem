package tntrun.eventhandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;

public class PlayerLeaveArenaChecker implements Listener {
   private TNTRun plugin;

   public PlayerLeaveArenaChecker(TNTRun plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onPlayerQuitEvent(PlayerQuitEvent e) {
      Player player = e.getPlayer();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         arena.getGameHandler().setPlaces(player.getName());
         arena.getPlayerHandler().leavePlayer(player, "", Messages.playerlefttoothers);
         if (arena.getPlayersManager().getPlayersCount() == 0) {
            arena.getGameHandler().stopArena();
         }

      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onPlayerDeathEvent(PlayerDeathEvent e) {
      Player player = e.getEntity();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         if (!this.plugin.getConfig().getBoolean("ondeath.dropitems", true)) {
            e.getDrops().clear();
         }

         arena.getGameHandler().setPlaces(player.getName());
         arena.getPlayerHandler().dispatchPlayer(player);
      }
   }
}
