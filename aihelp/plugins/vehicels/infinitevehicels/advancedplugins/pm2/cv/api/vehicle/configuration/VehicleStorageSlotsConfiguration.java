package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class VehicleStorageSlotsConfiguration implements ConfigurationSectionWritable {
   public static final VehicleStorageSlotsConfiguration DEFAULTS = new VehicleStorageSlotsConfiguration(0);
   private final int slots;

   public static VehicleStorageSlotsConfiguration load(ConfigurationSection var0) {
      int var1 = checkValue(var0.getInt("item-storage-slots", 0), "item-storage-slots");
      return new VehicleStorageSlotsConfiguration(var1);
   }

   private static int checkValue(int var0, String var1) {
      if (var0 >= 0) {
         return var0;
      } else {
         throw new InvalidConfigurationException(var1 + " must be >= 0");
      }
   }

   public VehicleStorageSlotsConfiguration(int var1) {
      Preconditions.checkArgument(var1 >= 0, "item-storage-slots cannot be negative.");
      this.slots = var1;
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("item-storage-slots", this.slots);
   }

   public static VehicleStorageSlotsConfiguration.VehicleStorageSlotsConfigurationBuilder builder() {
      return new VehicleStorageSlotsConfiguration.VehicleStorageSlotsConfigurationBuilder();
   }

   public int getSlots() {
      return this.slots;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof VehicleStorageSlotsConfiguration)) {
         return false;
      } else {
         VehicleStorageSlotsConfiguration var2 = (VehicleStorageSlotsConfiguration)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            return this.getSlots() == var2.getSlots();
         }
      }
   }

   protected boolean canEqual(Object var1) {
      return var1 instanceof VehicleStorageSlotsConfiguration;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getSlots();
      return var3;
   }

   public static class VehicleStorageSlotsConfigurationBuilder {
      private int slots;

      VehicleStorageSlotsConfigurationBuilder() {
      }

      public VehicleStorageSlotsConfiguration.VehicleStorageSlotsConfigurationBuilder slots(int var1) {
         this.slots = var1;
         return this;
      }

      public VehicleStorageSlotsConfiguration build() {
         return new VehicleStorageSlotsConfiguration(this.slots);
      }

      public String toString() {
         return "VehicleStorageSlotsConfiguration.VehicleStorageSlotsConfigurationBuilder(slots=" + this.slots + ")";
      }
   }
}
