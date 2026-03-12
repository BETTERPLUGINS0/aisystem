package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;

public final class ConfigMigrationHelper {
   public static boolean addSetting(DataContainer configData, String key, Object value) {
      Validate.notNull(configData, (String)"configData is null");
      Validate.notNull(key, (String)"key is null");
      if (!configData.contains(key)) {
         Log.info("  Adding setting '" + key + "' with value: " + String.valueOf(value));
         configData.set(key, value);
         return true;
      } else {
         return false;
      }
   }

   public static boolean removeSetting(DataContainer configData, String key) {
      Validate.notNull(configData, (String)"configData is null");
      Validate.notNull(key, (String)"key is null");
      if (configData.contains(key)) {
         Log.info("  Removing setting '" + key + "'. Previous value: " + String.valueOf(configData.get(key)));
         configData.remove(key);
         return true;
      } else {
         return false;
      }
   }

   public static boolean migrateValue(DataContainer configData, String key, Object expectedOldValue, Object newValue) {
      Validate.notNull(configData, (String)"configData is null");
      Validate.notNull(key, (String)"key is null");
      Validate.notNull(expectedOldValue, "expectedOldValue is null");
      if (configData.contains(key)) {
         Object oldValue = configData.get(key);
         if (expectedOldValue.equals(oldValue)) {
            Log.info("  Migrating setting '" + key + "' from value '" + String.valueOf(oldValue) + "' to new value '" + String.valueOf(newValue) + "'.");
            configData.set(key, newValue);
            return true;
         }
      }

      return false;
   }

   public static boolean migrateSetting(DataContainer configData, String oldKey, String newKey) {
      Validate.notNull(configData, (String)"configData is null");
      Validate.notNull(oldKey, (String)"oldKey is null");
      Validate.notNull(newKey, (String)"newKey is null");
      if (configData.contains(oldKey) && !configData.contains(newKey)) {
         Object oldValue = configData.get(oldKey);
         Log.info("  Migrating setting '" + oldKey + "' to '" + newKey + "'. Value: " + String.valueOf(oldValue));
         configData.set(newKey, oldValue);
         configData.remove(oldKey);
         return true;
      } else {
         return false;
      }
   }

   private ConfigMigrationHelper() {
   }
}
