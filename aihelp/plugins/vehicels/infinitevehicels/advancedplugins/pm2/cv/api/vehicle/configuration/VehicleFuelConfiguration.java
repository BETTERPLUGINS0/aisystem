package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class VehicleFuelConfiguration implements ConfigurationSectionWritable {
   public static final VehicleFuelConfiguration DEFAULTS = new VehicleFuelConfiguration(80.0F, 0.02F, 0.05F);
   private final float capacity;
   private final float minConsumption;
   private final float maxConsumption;

   public static VehicleFuelConfiguration load(ConfigurationSection var0) {
      float var1 = checkValue((float)var0.getDouble("capacity"), "capacity");
      float var2 = checkValue((float)var0.getDouble("min-consumption"), "minimum consumption");
      float var3 = checkValue((float)var0.getDouble("max-consumption"), "maximum consumption");
      return new VehicleFuelConfiguration(var1, var2, var3);
   }

   private static float checkValue(float var0, String var1) {
      if (var0 >= 0.0F) {
         return var0;
      } else {
         throw new InvalidConfigurationException(var1 + " cannot be negative");
      }
   }

   public VehicleFuelConfiguration(float var1, float var2, float var3) {
      Preconditions.checkArgument(var1 >= 0.0F, "capacity cannot be negative");
      Preconditions.checkArgument(var2 >= 0.0F, "minimum consumption cannot be negative");
      Preconditions.checkArgument(var3 >= 0.0F, "maximum consumption cannot be negative");
      this.capacity = var1;
      this.minConsumption = var2;
      this.maxConsumption = var3;
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("capacity", this.capacity);
      var1.set("min-consumption", this.minConsumption);
      var1.set("max-consumption", this.maxConsumption);
   }

   public float getCapacity() {
      return this.capacity;
   }

   public float getMinConsumption() {
      return this.minConsumption;
   }

   public float getMaxConsumption() {
      return this.maxConsumption;
   }
}
