package com.nisovin.shopkeepers.util.data.persistence.bukkit;

import com.nisovin.shopkeepers.util.bukkit.ConfigUtils;
import com.nisovin.shopkeepers.util.data.container.ConfigBasedDataContainer;
import com.nisovin.shopkeepers.util.data.persistence.DataStoreBase;
import com.nisovin.shopkeepers.util.data.persistence.InvalidDataFormatException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public class BukkitConfigDataStore extends ConfigBasedDataContainer implements DataStoreBase {
   public static BukkitConfigDataStore of(FileConfiguration bukkitConfig) {
      return new BukkitConfigDataStore(bukkitConfig);
   }

   public static BukkitConfigDataStore ofNewYamlConfig() {
      return of(ConfigUtils.newYamlConfig());
   }

   protected BukkitConfigDataStore(FileConfiguration config) {
      super(config);
   }

   public FileConfiguration getConfig() {
      return (FileConfiguration)super.getConfig();
   }

   public void loadFromString(String data) throws InvalidDataFormatException {
      try {
         ConfigUtils.loadConfigSafely(this.getConfig(), data);
      } catch (InvalidConfigurationException var3) {
         throw new InvalidDataFormatException("Failed to load data as Bukkit config!", var3);
      }
   }

   public String saveToString() {
      return this.getConfig().saveToString();
   }
}
