package advancedplugins.pm2.cv.api.vehicle.item.storage;

import advancedplugins.pm2.cv.api.vehicle.Vehicle;

public class VehicleItemStorage {
   private final VehicleItemHolder holder;

   public VehicleItemStorage(Vehicle var1, int var2) {
      this.holder = new VehicleItemHolder(var1, var2);
   }

   public VehicleItemHolder getHolder() {
      return this.holder;
   }
}
