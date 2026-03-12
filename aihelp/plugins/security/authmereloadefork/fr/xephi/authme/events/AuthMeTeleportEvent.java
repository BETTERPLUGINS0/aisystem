package fr.xephi.authme.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class AuthMeTeleportEvent extends AbstractTeleportEvent {
   private static final HandlerList handlers = new HandlerList();

   public AuthMeTeleportEvent(Player player, Location to) {
      super(false, player, to);
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
