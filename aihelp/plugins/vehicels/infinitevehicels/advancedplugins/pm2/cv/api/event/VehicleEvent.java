package advancedplugins.pm2.cv.api.event;

import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import org.jetbrains.annotations.NotNull;

public abstract class VehicleEvent extends InfiniteVehiclesEvent {
   @NotNull
   protected final Vehicle vehicle;

   public VehicleEvent(@NotNull Vehicle var1, boolean var2) {
      super(var2);
      this.vehicle = var1;
   }

   public VehicleEvent(@NotNull Vehicle var1) {
      this(var1, false);
   }

   @NotNull
   public Vehicle getVehicle() {
      return this.vehicle;
   }
}
