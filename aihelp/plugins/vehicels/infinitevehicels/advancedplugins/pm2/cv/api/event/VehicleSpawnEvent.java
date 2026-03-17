package advancedplugins.pm2.cv.api.event;

import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class VehicleSpawnEvent extends VehicleEvent implements Cancellable {
   private static final HandlerList HANDLERS = new HandlerList();
   private boolean cancelled = false;

   public VehicleSpawnEvent(@NotNull Vehicle var1) {
      super(var1, !Bukkit.isPrimaryThread());
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }
}
