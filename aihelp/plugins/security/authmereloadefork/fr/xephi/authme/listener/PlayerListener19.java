package fr.xephi.authme.listener;

import fr.xephi.authme.libs.javax.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerListener19 implements Listener {
   @Inject
   private ListenerService listenerService;

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }
}
