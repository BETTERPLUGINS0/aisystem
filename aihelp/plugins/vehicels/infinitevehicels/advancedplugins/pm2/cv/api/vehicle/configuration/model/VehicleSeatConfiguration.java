package advancedplugins.pm2.cv.api.vehicle.configuration.model;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class VehicleSeatConfiguration implements ConfigurationSectionWritable {
   private final boolean main;
   @NotNull
   private final Vector3D offset;

   public static VehicleSeatConfiguration load(@NotNull ConfigurationSection var0) {
      boolean var1 = var0.getBoolean("main");
      Vector3D var2 = (Vector3D)ConfigurationUtil.loadLibraryObject(Vector3D.class, var0, "offset");
      if (var2 == null) {
         throw new InvalidConfigurationException("seat requires offset to be set");
      } else {
         return new VehicleSeatConfiguration(var1, var2);
      }
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("main", this.main);
      ConfigurationUtil.writeLibraryObject(Vector3D.class, this.offset, var1.createSection("offset"));
   }

   public static VehicleSeatConfiguration.VehicleSeatConfigurationBuilder builder() {
      return new VehicleSeatConfiguration.VehicleSeatConfigurationBuilder();
   }

   public boolean isMain() {
      return this.main;
   }

   @NotNull
   public Vector3D getOffset() {
      return this.offset;
   }

   public VehicleSeatConfiguration(boolean var1, @NotNull Vector3D var2) {
      this.main = var1;
      this.offset = var2;
   }

   public static class VehicleSeatConfigurationBuilder {
      private boolean main;
      private Vector3D offset;

      VehicleSeatConfigurationBuilder() {
      }

      public VehicleSeatConfiguration.VehicleSeatConfigurationBuilder main(boolean var1) {
         this.main = var1;
         return this;
      }

      public VehicleSeatConfiguration.VehicleSeatConfigurationBuilder offset(@NotNull Vector3D var1) {
         this.offset = var1;
         return this;
      }

      public VehicleSeatConfiguration build() {
         return new VehicleSeatConfiguration(this.main, this.offset);
      }

      public String toString() {
         boolean var10000 = this.main;
         return "VehicleSeatConfiguration.VehicleSeatConfigurationBuilder(main=" + var10000 + ", offset=" + String.valueOf(this.offset) + ")";
      }
   }
}
