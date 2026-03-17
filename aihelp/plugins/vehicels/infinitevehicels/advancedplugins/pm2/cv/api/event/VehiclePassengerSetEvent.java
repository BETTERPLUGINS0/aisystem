package advancedplugins.pm2.cv.api.event;

import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleSeat;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehiclePassengerSetEvent extends VehicleEvent {
   private static final HandlerList HANDLERS = new HandlerList();
   @NotNull
   private final VehicleSeat vehicleSeat;
   @Nullable
   private final Entity previousOperator;
   @Nullable
   private final Entity newOperator;

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   public VehiclePassengerSetEvent(@NotNull Vehicle var1, @NotNull VehicleSeat var2, @Nullable Entity var3, @Nullable Entity var4) {
      super(var1, false);
      this.vehicleSeat = var2;
      this.previousOperator = var3;
      this.newOperator = var4;
   }

   @NotNull
   public VehicleSeat getSeat() {
      return this.vehicleSeat;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS;
   }

   @Nullable
   public Entity getPreviousOperator() {
      return this.previousOperator;
   }

   @Nullable
   public Entity getNewOperator() {
      return this.newOperator;
   }
}
