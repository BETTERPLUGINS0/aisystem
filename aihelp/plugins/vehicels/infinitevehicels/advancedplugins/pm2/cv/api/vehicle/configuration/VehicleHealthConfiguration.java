package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class VehicleHealthConfiguration implements ConfigurationSectionWritable {
   public static final VehicleHealthConfiguration DEFAULTS = new VehicleHealthConfiguration(false, 100.0F);
   private final boolean disabled;
   private final float maxHealth;

   public static VehicleHealthConfiguration load(@NotNull ConfigurationSection var0) {
      boolean var1 = var0.getBoolean("disable", false);
      float var2 = (float)var0.getDouble("max-health", 100.0D);
      if (var2 <= 0.0F) {
         throw new InvalidConfigurationException("max health cannot be zero or negative");
      } else {
         return new VehicleHealthConfiguration(var1, var2);
      }
   }

   public VehicleHealthConfiguration(boolean var1, float var2) {
      this.disabled = var1;
      this.maxHealth = var2;
   }

   public boolean isDisabled() {
      return this.disabled;
   }

   public float getMaxHealth() {
      return this.maxHealth;
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("disable", this.disabled);
      var1.set("max-health", this.maxHealth);
   }
}
