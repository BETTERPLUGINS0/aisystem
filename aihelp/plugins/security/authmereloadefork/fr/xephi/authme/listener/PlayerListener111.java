package fr.xephi.authme.listener;

import fr.xephi.authme.libs.javax.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityEvent;

public class PlayerListener111 implements Listener {
   @Inject
   private ListenerService listenerService;

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerAirChange(EntityAirChangeEvent event) {
      if (this.listenerService.shouldCancelEvent((EntityEvent)event)) {
         event.setCancelled(true);
      }

   }
}
