package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class DustOptionsParser extends DataParser {
   @NotNull
   public String getIdentifier() {
      return "dust-options";
   }

   @NotNull
   public Class<?> getType() {
      return DustOptions.class;
   }

   public Object parse(@NotNull ConfigurationSection var1) {
      int var2 = this.checkColorComponent(var1.getInt("red"), "red");
      int var3 = this.checkColorComponent(var1.getInt("green"), "green");
      int var4 = this.checkColorComponent(var1.getInt("blue"), "blue");
      float var5 = (float)var1.getDouble("size");
      return new DustOptions(Color.fromRGB(var2, var3, var4), var5);
   }

   private int checkColorComponent(int var1, String var2) {
      if (var1 >= 0 && var1 <= 255) {
         return var1;
      } else {
         throw new InvalidConfigurationException("invalid dust color " + var2 + " component. must be between 0 and 255");
      }
   }

   public void write(@NotNull Object var1, @NotNull ConfigurationSection var2) {
      super.write(var1, var2);
      if (var1 instanceof DustOptions) {
         DustOptions var3 = (DustOptions)var1;
         Color var4 = var3.getColor();
         var2.set("red", var4.getRed());
         var2.set("green", var4.getGreen());
         var2.set("blue", var4.getBlue());
         var2.set("size", var3.getSize());
      }

   }
}
