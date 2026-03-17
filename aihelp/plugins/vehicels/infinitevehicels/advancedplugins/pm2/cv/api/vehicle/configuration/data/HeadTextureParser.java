package advancedplugins.pm2.cv.api.vehicle.configuration.data;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HeadTextureParser extends DataParser {
   @NotNull
   public String getIdentifier() {
      return "head-texture";
   }

   @NotNull
   public Class<?> getType() {
      return HeadTexture.class;
   }

   @Nullable
   public HeadTexture parse(@NotNull ConfigurationSection var1) {
      String var2 = var1.getString("value");
      return var2 != null ? new HeadTexture(var2) : null;
   }

   public void write(@NotNull Object var1, @NotNull ConfigurationSection var2) {
      super.write(var1, var2);
      if (var1 instanceof HeadTexture) {
         var2.set("value", ((HeadTexture)var1).getValue());
      }

   }
}
