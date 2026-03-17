package advancedplugins.pm2.cv.api.event;

import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class VehicleStateChangeEvent extends VehicleEvent {
   private static final HandlerList HANDLERS = new HandlerList();
   @NotNull
   private final VehicleState previousState;
   @NotNull
   private final VehicleState newState;

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   public VehicleStateChangeEvent(@NotNull Vehicle var1, @NotNull VehicleState var2, @NotNull VehicleState var3) {
      super(var1, false);
      this.previousState = var2;
      this.newState = var3;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS;
   }

   @NotNull
   public VehicleState getPreviousState() {
      return this.previousState;
   }

   @NotNull
   public VehicleState getNewState() {
      return this.newState;
   }
}
