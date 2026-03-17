package advancedplugins.pm2.cv.api.vehicle.configuration.model;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.VehicleProjectile;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleProjectileShooterConfiguration implements ConfigurationSectionWritable {
   @NotNull
   private final Vector3D offset;
   @NotNull
   private final VehicleProjectile vehicleProjectile;
   @NotNull
   private final PlayerInput.InputType bind;
   @Nullable
   private PlayerInput.InputType secondaryBind;
   private float yaw;
   private float pitch;
   private int cooldown;

   public static VehicleProjectileShooterConfiguration load(@NotNull ConfigurationSection var0) {
      Vector3D var1 = (Vector3D)ConfigurationUtil.loadLibraryObject(Vector3D.class, var0, "offset");
      if (var1 == null) {
         throw new InvalidConfigurationException("projectile shooter requires offset to be set");
      } else {
         VehicleProjectile var2 = null;

         try {
            var2 = VehicleProjectile.read(var0.getConfigurationSection("projectile"));
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         if (var2 == null) {
            throw new InvalidConfigurationException("projectile shooter requires projectile to be shot");
         } else {
            PlayerInput.InputType var3 = (PlayerInput.InputType)ConfigurationUtil.loadEnum(PlayerInput.InputType.class, var0, "shoot-bind");
            if (var3 == null) {
               var3 = PlayerInput.InputType.RIGHT_CLICK;
            }

            PlayerInput.InputType var4 = (PlayerInput.InputType)ConfigurationUtil.loadEnum(PlayerInput.InputType.class, var0, "shoot-secondary-bind");
            float var5 = (float)var0.getDouble("yaw", 0.0D);
            float var6 = (float)var0.getDouble("pitch", 0.0D);
            int var7 = var0.getInt("shoot-cooldown", 0);
            return new VehicleProjectileShooterConfiguration(var1, var2, var3, var4, var5, var6, var7);
         }
      }
   }

   public void write(@NotNull ConfigurationSection var1) {
      ConfigurationUtil.writeLibraryObject(Vector3D.class, this.offset, var1.createSection("offset"));
      ConfigurationUtil.writeEnum(this.bind, var1, "shoot-bind");
      var1.set("yaw", this.yaw);
      var1.set("pitch", this.pitch);
      if (this.secondaryBind != null) {
         ConfigurationUtil.writeEnum(this.secondaryBind, var1, "shoot-secondary-bind");
      }

      var1.set("shoot-cooldown", this.cooldown);
      this.vehicleProjectile.write(var1.createSection("projectile"));
   }

   public static VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder builder() {
      return new VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder();
   }

   @NotNull
   public Vector3D getOffset() {
      return this.offset;
   }

   @NotNull
   public VehicleProjectile getVehicleProjectile() {
      return this.vehicleProjectile;
   }

   @NotNull
   public PlayerInput.InputType getBind() {
      return this.bind;
   }

   @Nullable
   public PlayerInput.InputType getSecondaryBind() {
      return this.secondaryBind;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public int getCooldown() {
      return this.cooldown;
   }

   public VehicleProjectileShooterConfiguration(@NotNull Vector3D var1, @NotNull VehicleProjectile var2, @NotNull PlayerInput.InputType var3) {
      this.offset = var1;
      this.vehicleProjectile = var2;
      this.bind = var3;
   }

   public VehicleProjectileShooterConfiguration(@NotNull Vector3D var1, @NotNull VehicleProjectile var2, @NotNull PlayerInput.InputType var3, @Nullable PlayerInput.InputType var4, float var5, float var6, int var7) {
      this.offset = var1;
      this.vehicleProjectile = var2;
      this.bind = var3;
      this.secondaryBind = var4;
      this.yaw = var5;
      this.pitch = var6;
      this.cooldown = var7;
   }

   public static class VehicleProjectileShooterConfigurationBuilder {
      private Vector3D offset;
      private VehicleProjectile vehicleProjectile;
      private PlayerInput.InputType bind;
      private PlayerInput.InputType secondaryBind;
      private float yaw;
      private float pitch;
      private int cooldown;

      VehicleProjectileShooterConfigurationBuilder() {
      }

      public VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder offset(@NotNull Vector3D var1) {
         this.offset = var1;
         return this;
      }

      public VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder vehicleProjectile(@NotNull VehicleProjectile var1) {
         this.vehicleProjectile = var1;
         return this;
      }

      public VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder bind(@NotNull PlayerInput.InputType var1) {
         this.bind = var1;
         return this;
      }

      public VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder secondaryBind(@Nullable PlayerInput.InputType var1) {
         this.secondaryBind = var1;
         return this;
      }

      public VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder yaw(float var1) {
         this.yaw = var1;
         return this;
      }

      public VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder pitch(float var1) {
         this.pitch = var1;
         return this;
      }

      public VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder cooldown(int var1) {
         this.cooldown = var1;
         return this;
      }

      public VehicleProjectileShooterConfiguration build() {
         return new VehicleProjectileShooterConfiguration(this.offset, this.vehicleProjectile, this.bind, this.secondaryBind, this.yaw, this.pitch, this.cooldown);
      }

      public String toString() {
         String var10000 = String.valueOf(this.offset);
         return "VehicleProjectileShooterConfiguration.VehicleProjectileShooterConfigurationBuilder(offset=" + var10000 + ", vehicleProjectile=" + String.valueOf(this.vehicleProjectile) + ", bind=" + String.valueOf(this.bind) + ", secondaryBind=" + String.valueOf(this.secondaryBind) + ", yaw=" + this.yaw + ", pitch=" + this.pitch + ", cooldown=" + this.cooldown + ")";
      }
   }
}
