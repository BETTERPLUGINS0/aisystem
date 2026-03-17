package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DataParser {
   public static DataParser getParser(@NotNull ConfigurationSection var0) {
      return DataParsers.getParser(var0.getString("data-type", ""));
   }

   @NotNull
   public abstract String getIdentifier();

   @NotNull
   public abstract Class<?> getType();

   @Nullable
   public abstract Object parse(@NotNull ConfigurationSection var1);

   public void write(@NotNull Object var1, @NotNull ConfigurationSection var2) {
      this.writeIdentifier(var2);
   }

   protected void writeIdentifier(@NotNull ConfigurationSection var1) {
      var1.set("data-type", this.getIdentifier().toLowerCase().trim());
   }
}
