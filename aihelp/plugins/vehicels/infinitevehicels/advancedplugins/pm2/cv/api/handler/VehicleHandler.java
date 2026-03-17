package advancedplugins.pm2.cv.api.handler;

import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface VehicleHandler extends PluginHandler {
   @NotNull
   Set<Vehicle> getRegisteredVehicles();

   @Nullable
   Vehicle getVehicleByOperator(@NotNull Entity var1);

   @NotNull
   Vehicle spawnVehicle(@NotNull VehicleConfiguration var1, @NotNull World var2, double var3, double var5, double var7, @Nullable UUID var9, @Nullable UUID var10);

   @NotNull
   default Vehicle spawnVehicle(@NotNull VehicleConfiguration configuration, @NotNull Location location, @Nullable UUID ownerUUID) {
      World world = (World)Objects.requireNonNull(location.getWorld(), "the provided location returned a null world");
      return this.spawnVehicle(configuration, world, location.getX(), location.getY(), location.getZ(), (UUID)null, ownerUUID);
   }

   void openVehicleGui(@NotNull Vehicle var1, @NotNull Player var2);

   void pickupVehicle(@NotNull Vehicle var1, @NotNull Player var2, boolean var3, boolean var4);

   void removeVehicle(@NotNull Vehicle var1);

   void destroyVehicle(@NotNull Vehicle var1);

   void register(@NotNull Vehicle var1);

   void unregister(@NotNull Vehicle var1, boolean var2);

   default void unregister(@NotNull Vehicle vehicle) {
      this.unregister(vehicle, false);
   }
}
