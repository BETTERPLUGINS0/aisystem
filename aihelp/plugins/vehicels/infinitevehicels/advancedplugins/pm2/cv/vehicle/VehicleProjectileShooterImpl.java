package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleProjectileShooter;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleProjectileShooterConfiguration;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import advancedplugins.pm2.cv.util.Constants;
import advancedplugins.pm2.cv.util.math.CachedMathUtil;
import advancedplugins.pm2.cv.util.math.TrigonometryUtil;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.geometry.euclidean.twod.Vector2D;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class VehicleProjectileShooterImpl implements VehicleProjectileShooter {
   private final Vehicle vehicle;
   private final VehicleProjectileShooterConfiguration configuration;
   private int rotation;
   private int angle;
   private double x;
   private double y;
   private double z;
   private float hOffset;
   private float vOffset;

   public VehicleProjectileShooterImpl(VehicleImpl vehicle, VehicleProjectileShooterConfiguration configuration) {
      this.vehicle = var1;
      this.configuration = var2;
      Vector3D var3 = var2.getOffset();
      Vector2D var4 = new Vector2D(var3.getX(), var3.getZ());
      this.angle = TrigonometryUtil.calculateAngleBetween(var4, Constants.TOWARD_Z_2D);
      this.hOffset = (float)FastMath.sqrt(var3.getX() * var3.getX() + var3.getZ() * var3.getZ());
      this.vOffset = (float)var3.getY();
   }

   @NotNull
   public VehicleProjectileShooterConfiguration getConfiguration() {
      return this.configuration;
   }

   @NotNull
   public Vehicle getVehicle() {
      return this.vehicle;
   }

   public void spawnProjectile(Location spawnLocation) {
      this.getConfiguration().getVehicleProjectile().copy().spawn(var1, this.getVehicle());
   }

   public void shoot() {
      Run.sync(() -> {
         this.recalculateLocation();
         float var1 = this.vehicle.getLocation().getYaw();
         float var2 = (var1 + this.configuration.getYaw()) % 360.0F;
         Location var3 = new Location(this.vehicle.getWorld(), this.x, this.y, this.z, var2, this.configuration.getPitch());
         this.getConfiguration().getVehicleProjectile().copy().spawn(var3, this.getVehicle());
      });
   }

   private void recalculateLocation() {
      this.rotation = TrigonometryUtil.toIntAngle(this.vehicle.getRotation());
      int var1 = this.rotation + this.angle + 90;
      this.y = this.vehicle.getY() + (double)this.vOffset + (double)(this.vehicle.getConfiguration().model().getModelID() != null ? this.vehicle.getConfiguration().model().getModelOffset() : 0.0F);
      this.x = this.vehicle.getX() + (double)(CachedMathUtil.cos(var1) * this.hOffset);
      this.z = this.vehicle.getZ() + (double)(CachedMathUtil.sin(var1) * this.hOffset);
   }

   public PlayerInput.InputType getBind() {
      return this.configuration.getBind();
   }

   public int getCooldown() {
      return this.configuration.getCooldown();
   }

   public PlayerInput.InputType getSecondaryBind() {
      return this.configuration.getSecondaryBind();
   }

   public int getRotation() {
      return this.rotation;
   }

   public int getAngle() {
      return this.angle;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public float getHOffset() {
      return this.hOffset;
   }

   public float getVOffset() {
      return this.vOffset;
   }
}
