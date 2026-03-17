package advancedplugins.pm2.cv.api.registry.types;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.registry.ConfigurationRegistryBase;
import advancedplugins.pm2.cv.api.util.Constants;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleModelConfiguration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public final class VehicleModelConfigurationRegistry extends ConfigurationRegistryBase<VehicleModelConfiguration> {
   private static final Set<VehicleModelConfiguration> DEFAULTS = new HashSet();

   @NotNull
   protected File getFolder() {
      return Constants.Files.VEHICLE_MODELS_FOLDER;
   }

   protected VehicleModelConfiguration loadEntry(File var1) {
      try {
         return VehicleModelConfiguration.load(this.load(var1));
      } catch (Exception var3) {
         InfiniteVehicles.getPlugin().getLogger().severe("couldn't load vehicle model: " + var1.getName());
         throw var3;
      }
   }

   private YamlConfiguration load(File var1) {
      try {
         String var2 = this.saveToString(new YamlConfiguration(), var1);
         YamlConfiguration var3 = new YamlConfiguration();
         var3.loadFromString(var2);
         return var3;
      } catch (Exception var4) {
         InfiniteVehicles.getPlugin().getLogger().severe("couldn't load vehicle model: " + var1.getName());
         throw new RuntimeException(var4);
      }
   }

   @NotNull
   public String saveToString(YamlConfiguration var1, File var2) {
      InputStreamReader var3 = new InputStreamReader(new FileInputStream(var2), StandardCharsets.UTF_8);
      BufferedReader var4 = new BufferedReader(var3);
      StringBuilder var5 = new StringBuilder();

      String var6;
      try {
         while((var6 = var4.readLine()) != null) {
            if (var6.startsWith("#") || !var6.contains("==: Pattern")) {
               var5.append(var6);
               var5.append('\n');
            }
         }
      } finally {
         var4.close();
      }

      return var5.toString();
   }

   protected Set<VehicleModelConfiguration> getDefaults() {
      return DEFAULTS;
   }

   protected void writeEntry(@NotNull VehicleModelConfiguration var1, YamlConfiguration var2) {
      var1.write(var2);
   }
}
