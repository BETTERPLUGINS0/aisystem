package advancedplugins.pm2.cv.api.vehicle.configuration.model;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class VehicleHitBoxConfiguration implements ConfigurationSectionWritable {
   private final double width;
   private final double height;
   private final double depth;

   public static VehicleHitBoxConfiguration load(ConfigurationSection var0) {
      double var1 = var0.getDouble("width");
      double var3 = var0.getDouble("height");
      double var5 = var0.getDouble("depth");
      if (!(var1 <= 0.0D) && !(var3 <= 0.0D) && !(var5 <= 0.0D)) {
         return new VehicleHitBoxConfiguration(var1, var3, var5);
      } else {
         throw new InvalidConfigurationException("hitbox width, height and depth must all be >= 0.5");
      }
   }

   public VehicleHitBoxConfiguration(double var1, double var3, double var5) {
      Preconditions.checkArgument(var1 > 0.0D, "width must be >= 0.5");
      Preconditions.checkArgument(var3 > 0.0D, "height must be >= 0.5");
      Preconditions.checkArgument(var5 > 0.0D, "depth must be >= 0.5");
      this.width = var1;
      this.height = var3;
      this.depth = var5;
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("width", this.width);
      var1.set("height", this.height);
      var1.set("depth", this.depth);
   }

   public static VehicleHitBoxConfiguration.VehicleHitBoxConfigurationBuilder builder() {
      return new VehicleHitBoxConfiguration.VehicleHitBoxConfigurationBuilder();
   }

   public double getWidth() {
      return this.width;
   }

   public double getHeight() {
      return this.height;
   }

   public double getDepth() {
      return this.depth;
   }

   public static class VehicleHitBoxConfigurationBuilder {
      private double width;
      private double height;
      private double depth;

      VehicleHitBoxConfigurationBuilder() {
      }

      public VehicleHitBoxConfiguration.VehicleHitBoxConfigurationBuilder width(double var1) {
         this.width = var1;
         return this;
      }

      public VehicleHitBoxConfiguration.VehicleHitBoxConfigurationBuilder height(double var1) {
         this.height = var1;
         return this;
      }

      public VehicleHitBoxConfiguration.VehicleHitBoxConfigurationBuilder depth(double var1) {
         this.depth = var1;
         return this;
      }

      public VehicleHitBoxConfiguration build() {
         return new VehicleHitBoxConfiguration(this.width, this.height, this.depth);
      }

      public String toString() {
         return "VehicleHitBoxConfiguration.VehicleHitBoxConfigurationBuilder(width=" + this.width + ", height=" + this.height + ", depth=" + this.depth + ")";
      }
   }
}
