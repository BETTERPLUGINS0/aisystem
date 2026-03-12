package fr.xephi.authme.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class SpawnTeleportEvent extends AbstractTeleportEvent {
   private static final HandlerList handlers = new HandlerList();
   private final boolean isAuthenticated;

   public SpawnTeleportEvent(Player player, Location to, boolean isAuthenticated) {
      super(false, player, to);
      this.isAuthenticated = isAuthenticated;
   }

   public boolean isAuthenticated() {
      return this.isAuthenticated;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
