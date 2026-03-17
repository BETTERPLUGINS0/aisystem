package advancedplugins.pm2.cv.api.registry.types;

import advancedplugins.pm2.cv.api.registry.ConfigurationRegistryBase;
import advancedplugins.pm2.cv.api.util.Constants;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import java.io.File;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VehicleConfigurationRegistry extends ConfigurationRegistryBase<VehicleConfiguration> {
   @NotNull
   protected File getFolder() {
      return Constants.Files.VEHICLES_FOLDER;
   }

   protected VehicleConfiguration loadEntry(File var1) {
      return VehicleConfiguration.load(YamlConfiguration.loadConfiguration(var1));
   }

   @Nullable
   protected Set<VehicleConfiguration> getDefaults() {
      return null;
   }

   protected void writeEntry(@NotNull VehicleConfiguration var1, YamlConfiguration var2) {
      var1.write(var2);
   }
}
