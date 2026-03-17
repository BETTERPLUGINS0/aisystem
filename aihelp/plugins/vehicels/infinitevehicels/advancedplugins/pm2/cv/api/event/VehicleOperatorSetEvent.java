package advancedplugins.pm2.cv.api.event;

import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleOperatorSetEvent extends VehicleEvent {
   private static final HandlerList HANDLERS = new HandlerList();
   @Nullable
   private final Entity previousOperator;
   @Nullable
   private final Entity newOperator;

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS;
   }

   public VehicleOperatorSetEvent(@NotNull Vehicle var1, @Nullable Entity var2, @Nullable Entity var3) {
      super(var1, false);
      this.previousOperator = var2;
      this.newOperator = var3;
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
