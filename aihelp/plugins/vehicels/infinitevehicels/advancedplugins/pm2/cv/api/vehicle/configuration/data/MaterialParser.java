package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import advancedplugins.pm2.cv.api.util.reflection.EnumReflection;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MaterialParser extends DataParser {
   @NotNull
   public String getIdentifier() {
      return "material";
   }

   @NotNull
   public Class<?> getType() {
      return Material.class;
   }

   @Nullable
   public Material parse(@NotNull ConfigurationSection var1) {
      return (Material)EnumReflection.getEnumConstant(Material.class, var1.getString("value", "").trim().toUpperCase());
   }

   public void write(@NotNull Object var1, @NotNull ConfigurationSection var2) {
      super.write(var1, var2);
      if (var1 instanceof Material) {
         var2.set("value", ((Material)var1).name());
      }

   }
}
