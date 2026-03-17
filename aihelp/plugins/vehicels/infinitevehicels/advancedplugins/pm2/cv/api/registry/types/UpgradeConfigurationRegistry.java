package advancedplugins.pm2.cv.api.registry.types;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.registry.ConfigurationRegistryBase;
import advancedplugins.pm2.cv.api.upgrade.UpgradeConfiguration;
import advancedplugins.pm2.cv.api.util.Constants;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpgradeConfigurationRegistry extends ConfigurationRegistryBase<UpgradeConfiguration> {
   @NotNull
   protected File getFolder() {
      return Constants.Files.UPGRADES_FOLDER;
   }

   protected UpgradeConfiguration loadEntry(File var1) {
      return UpgradeConfiguration.load(YamlConfiguration.loadConfiguration(var1));
   }

   public void load() {
      File var1 = this.getFolder();
      if (var1.exists()) {
         if (var1.listFiles() != null) {
            File[] var2 = (File[])Objects.requireNonNull(var1.listFiles());
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               File var5 = var2[var4];
               if (var5.getName().endsWith(".yml") || var5.getName().endsWith(".yaml")) {
                  try {
                     UpgradeConfiguration var6 = this.loadEntry(var5);
                     if (var6 == null) {
                        InfiniteVehicles.getPlugin().getLogger().warning("Failed to load upgrade at: " + var5.getName());
                     } else {
                        this.register(var6);
                     }
                  } catch (Exception var7) {
                     InfiniteVehicles.getPlugin().getLogger().warning("Failed to load upgrade at: " + var5.getName());
                     var7.printStackTrace();
                  }
               }
            }

         }
      }
   }

   @Nullable
   protected Set<UpgradeConfiguration> getDefaults() {
      return new HashSet();
   }

   protected void writeEntry(@NotNull UpgradeConfiguration var1, YamlConfiguration var2) {
      var1.write(var2);
   }
}
