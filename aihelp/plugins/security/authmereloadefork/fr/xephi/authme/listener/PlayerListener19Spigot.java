package fr.xephi.authme.listener;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.TeleportationService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerListener19Spigot implements Listener {
   private static boolean isPlayerSpawnLocationEventCalled = false;
   @Inject
   private TeleportationService teleportationService;

   public static boolean isPlayerSpawnLocationEventCalled() {
      return isPlayerSpawnLocationEventCalled;
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
      isPlayerSpawnLocationEventCalled = true;
      Player player = event.getPlayer();
      Location customSpawnLocation = this.teleportationService.prepareOnJoinSpawnLocation(player);
      if (customSpawnLocation != null) {
         event.setSpawnLocation(customSpawnLocation);
      }

   }
}
