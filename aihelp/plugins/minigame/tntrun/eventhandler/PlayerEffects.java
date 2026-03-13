package tntrun.eventhandler;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import tntrun.TNTRun;
import tntrun.arena.Arena;

public class PlayerEffects implements Listener {
   private TNTRun plugin;

   public PlayerEffects(TNTRun plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onPlayerMoveEvent(PlayerMoveEvent e) {
      Player player = e.getPlayer();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         if (this.plugin.useStats()) {
            Location location = player.getLocation();
            player.getWorld().spawnParticle(Particle.WITCH, location, 0);
         }
      }
   }
}
