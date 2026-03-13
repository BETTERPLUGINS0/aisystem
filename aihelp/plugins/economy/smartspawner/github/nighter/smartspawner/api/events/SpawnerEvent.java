package github.nighter.smartspawner.api.events;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class SpawnerEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final Location location;
   private final int quantity;

   protected SpawnerEvent(Location location, int quantity) {
      this.location = location;
      this.quantity = quantity;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @Generated
   public Location getLocation() {
      return this.location;
   }

   @Generated
   public int getQuantity() {
      return this.quantity;
   }
}
