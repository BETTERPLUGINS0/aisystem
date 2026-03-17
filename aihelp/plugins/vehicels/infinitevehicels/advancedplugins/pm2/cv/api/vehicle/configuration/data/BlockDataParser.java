package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import advancedplugins.pm2.cv.api.util.reflection.EnumReflection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class BlockDataParser extends DataParser {
   @NotNull
   public String getIdentifier() {
      return "block-data";
   }

   @NotNull
   public Class<?> getType() {
      return BlockData.class;
   }

   public BlockData parse(@NotNull ConfigurationSection var1) {
      String var2 = var1.getString("value", "");

      try {
         return Bukkit.createBlockData(var2);
      } catch (IllegalArgumentException var5) {
         Material var4 = (Material)EnumReflection.getEnumConstant(Material.class, var2.trim().toUpperCase());
         return var4 != null ? var4.createBlockData() : null;
      }
   }

   public void write(@NotNull Object var1, @NotNull ConfigurationSection var2) {
      super.write(var1, var2);
      if (var1 instanceof BlockData) {
         var2.set("value", ((BlockData)var1).getAsString(true));
      }

   }
}
