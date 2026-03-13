package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.config.lib.ConfigLoadException;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Arrays;
import java.util.List;

public class ConfigMigrations {
   private static final String CONFIG_VERSION_KEY = "config-version";
   private static final int FIRST_VERSION = 0;
   private static final List<? extends ConfigMigration> migrations = Arrays.asList(new ConfigMigration1(), new ConfigMigration2(), new ConfigMigration3(), new ConfigMigration4(), new ConfigMigration5(), new ConfigMigration6(), new ConfigMigration7(), new ConfigMigration8(), new ConfigMigration9(), new ConfigMigration10());

   public static int getLatestVersion() {
      return migrations.size();
   }

   public static boolean applyMigrations(DataContainer configData) throws ConfigLoadException {
      if (configData.isEmpty()) {
         return false;
      } else {
         int configVersion;
         if (!configData.contains("config-version")) {
            Log.info("Missing config version. Assuming version '0'.");
            configVersion = 0;
         } else {
            Object rawConfigVersion = configData.get("config-version");
            Integer configVersionI = ConversionUtils.toInteger(rawConfigVersion);
            if (configVersionI == null) {
               throw new ConfigLoadException("Could not parse config version: " + String.valueOf(rawConfigVersion));
            }

            configVersion = configVersionI;
         }

         if (configVersion < 0) {
            throw new ConfigLoadException("Invalid config version: " + configVersion);
         } else if (configVersion > getLatestVersion()) {
            throw new ConfigLoadException("Invalid config version: " + configVersion + " (the latest version is " + getLatestVersion() + ")");
         } else {
            boolean migrated = false;

            for(int version = configVersion; version < migrations.size(); ++version) {
               int nextVersion = version + 1;
               ConfigMigration migration = (ConfigMigration)migrations.get(version);

               assert migration != null;

               Log.info("Migrating config from version " + version + " to version " + nextVersion + " ...");

               try {
                  migration.apply(configData);
               } catch (Exception var7) {
                  throw new ConfigLoadException("Config migration failed with an error!", var7);
               }

               configData.set((String)"config-version", nextVersion);
               migrated = true;
               Log.info("Config migrated to version " + nextVersion + ".");
            }

            return migrated;
         }
      }
   }

   private ConfigMigrations() {
   }
}
