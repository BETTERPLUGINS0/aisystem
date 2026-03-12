package fr.xephi.authme.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class FirstSpawnTeleportEvent extends AbstractTeleportEvent {
   private static final HandlerList handlers = new HandlerList();

   public FirstSpawnTeleportEvent(Player player, Location to) {
      super(false, player, to);
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
