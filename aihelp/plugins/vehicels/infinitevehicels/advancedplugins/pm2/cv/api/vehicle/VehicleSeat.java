package advancedplugins.pm2.cv.api.vehicle;

import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleSeatConfiguration;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VehicleSeat {
   @NotNull
   VehicleSeatConfiguration getConfiguration();

   @NotNull
   Vehicle getVehicle();

   boolean isMain();

   boolean isSpawned();

   boolean isDestroyed();

   void spawn();

   void destroy();

   double getX();

   double getY();

   double getZ();

   float getRotation();

   @Nullable
   Entity getPassenger();

   default boolean isOccupied() {
      return this.getPassenger() != null;
   }

   void setPassenger(@Nullable Entity var1);

   void moveToWorld(@NotNull World var1);

   ArmorStand getBone();
}
