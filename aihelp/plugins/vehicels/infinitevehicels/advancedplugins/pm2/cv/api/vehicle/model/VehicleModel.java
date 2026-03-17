package advancedplugins.pm2.cv.api.vehicle.model;

import advancedplugins.pm2.cv.api.interfaces.Tickable;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleModelConfiguration;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VehicleModel<C extends VehicleModelConfiguration> extends Tickable {
   @NotNull
   C getConfiguration();

   boolean isSpawned();

   void spawn();

   void destroy();

   void despawnParts();

   void setState(@Nullable VehicleState var1);

   @NotNull
   Location getLocation();

   @NotNull
   World getWorld();

   double getX();

   double getY();

   double getZ();

   float getRotation();

   void setRotation(float var1);

   void setLocationAndRotation(double var1, double var3, double var5, float var7);

   void setLocationAndRotation(double var1, double var3, double var5, float var7, boolean var8);

   void setLocation(double var1, double var3, double var5, boolean var7);

   void setLocation(double var1, double var3, double var5);
}
