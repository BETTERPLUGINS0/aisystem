package fr.xephi.authme.listener;

import fr.xephi.authme.libs.javax.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
   @Inject
   private ListenerService listenerService;

   @EventHandler(
      ignoreCancelled = true
   )
   public void onBlockPlace(BlockPlaceEvent event) {
      if (this.listenerService.shouldCancelEvent(event.getPlayer())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onBlockBreak(BlockBreakEvent event) {
      if (this.listenerService.shouldCancelEvent(event.getPlayer())) {
         event.setCancelled(true);
      }

   }
}
