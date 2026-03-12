package fr.xephi.authme.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public abstract class AbstractTeleportEvent extends CustomEvent implements Cancellable {
   private final Player player;
   private final Location from;
   private Location to;
   private boolean isCancelled;

   public AbstractTeleportEvent(boolean isAsync, Player player, Location to) {
      super(isAsync);
      this.player = player;
      this.from = player.getLocation();
      this.to = to;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Location getFrom() {
      return this.from;
   }

   public void setTo(Location to) {
      this.to = to;
   }

   public Location getTo() {
      return this.to;
   }

   public void setCancelled(boolean isCancelled) {
      this.isCancelled = isCancelled;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }
}
