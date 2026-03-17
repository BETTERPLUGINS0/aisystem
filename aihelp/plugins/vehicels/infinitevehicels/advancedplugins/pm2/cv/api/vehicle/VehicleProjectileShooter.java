package advancedplugins.pm2.cv.api.vehicle;

import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleProjectileShooterConfiguration;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface VehicleProjectileShooter {
   @NotNull
   VehicleProjectileShooterConfiguration getConfiguration();

   @NotNull
   Vehicle getVehicle();

   void spawnProjectile(Location var1);

   void shoot();

   PlayerInput.InputType getBind();

   PlayerInput.InputType getSecondaryBind();
}
