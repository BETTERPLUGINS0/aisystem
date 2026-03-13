package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.LogDetectionHandler;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ConfigUtils {
   private static final ThreadLocal<YamlConfiguration> YAML_CONFIG = ThreadLocal.withInitial(ConfigUtils::newYamlConfig);
   private static final LogDetectionHandler ERROR_DETECTION_HANDLER = new LogDetectionHandler();

   public static YamlConfiguration newYamlConfig() {
      YamlConfiguration config = new YamlConfiguration();
      disablePathSeparator(config);
      return config;
   }

   public static void disablePathSeparator(Configuration config) {
      config.options().pathSeparator('\u0000');
   }

   public static Map<String, Object> getValues(ConfigurationSection section) {
      return section.getValues(false);
   }

   public static void convertSubSectionsToMaps(ConfigurationSection rootSection) {
      rootSection.getValues(false).forEach((key, value) -> {
         assert key != null;

         if (value instanceof ConfigurationSection) {
            ConfigurationSection section = (ConfigurationSection)value;
            Map<String, Object> innerSectionMap = getValues(section);
            convertSectionsToMaps(innerSectionMap);
            rootSection.set(key, innerSectionMap);
         }

      });
   }

   public static Map<String, Object> convertSectionsToMaps(ConfigurationSection rootSection) {
      Map<String, Object> sectionMap = getValues(rootSection);
      convertSectionsToMaps(sectionMap);
      return sectionMap;
   }

   public static void convertSectionsToMaps(Map<? extends String, Object> rootMap) {
      rootMap.entrySet().forEach((entry) -> {
         Object value = entry.getValue();
         if (value instanceof ConfigurationSection) {
            ConfigurationSection section = (ConfigurationSection)value;
            Map<String, Object> innerSectionMap = getValues(section);
            convertSectionsToMaps(innerSectionMap);
            entry.setValue(innerSectionMap);
         }

      });
   }

   public static void clearConfigSection(ConfigurationSection configSection) {
      Validate.notNull(configSection, (String)"configSection is null");
      configSection.getKeys(false).forEach((key) -> {
         assert key != null;

         configSection.set(key, (Object)null);
      });
   }

   public static void setAll(ConfigurationSection configSection, Map<?, ?> map) {
      Validate.notNull(configSection, (String)"configSection is null");
      Validate.notNull(map, (String)"map is null");
      map.forEach((key, value) -> {
         String stringKey = StringUtils.toStringOrNull(key);
         if (stringKey != null) {
            configSection.set(stringKey, value);
         }

      });
   }

   public static Map<String, Object> serialize(ConfigurationSerializable serializable) {
      Validate.notNull(serializable, (String)"serializable is null");
      Map<String, Object> dataMap = new LinkedHashMap();
      dataMap.put("==", ConfigurationSerialization.getAlias(serializable.getClass()));
      dataMap.putAll(serializable.serialize());
      return dataMap;
   }

   @Nullable
   public static <T extends ConfigurationSerializable> T deserialize(@Nullable Map<? extends String, ?> dataMap) {
      if (dataMap == null) {
         return null;
      } else {
         try {
            return (ConfigurationSerializable)Unsafe.cast(ConfigurationSerialization.deserializeObject((Map)Unsafe.castNonNull(dataMap)));
         } catch (IllegalArgumentException var2) {
            throw new IllegalArgumentException("Could not deserialize object", var2);
         }
      }
   }

   public static Map<String, Object> serializeDeeply(ConfigurationSerializable serializable) {
      Validate.notNull(serializable, (String)"serializable is null");
      Map<String, Object> dataMap = serialize(serializable);
      serializeDeeply(dataMap);
      return dataMap;
   }

   public static void serializeDeeply(@Nullable Map<?, Object> dataMap) {
      if (dataMap != null) {
         dataMap.entrySet().forEach((entry) -> {
            Object value = entry.getValue();
            if (value instanceof Map) {
               Map<?, Object> innerMap = new LinkedHashMap((Map)value);
               serializeDeeply((Map)innerMap);
               entry.setValue(innerMap);
            } else {
               Map innerSerializableData;
               if (value instanceof ConfigurationSection) {
                  ConfigurationSection section = (ConfigurationSection)value;
                  innerSerializableData = getValues(section);
                  serializeDeeply(innerSerializableData);
                  entry.setValue(innerSerializableData);
               } else if (value instanceof ConfigurationSerializable) {
                  ConfigurationSerializable serializable = (ConfigurationSerializable)value;
                  innerSerializableData = serializeDeeply(serializable);
                  entry.setValue(innerSerializableData);
               }
            }

         });
      }
   }

   public static String toFlatConfigYaml(Map<?, ?> map) {
      YamlConfiguration yamlConfig = (YamlConfiguration)YAML_CONFIG.get();

      String var2;
      try {
         setAll(yamlConfig, map);
         var2 = yamlConfig.saveToString();
      } finally {
         clearConfigSection(yamlConfig);
      }

      return var2;
   }

   public static String toConfigYaml(String key, @Nullable Object object) {
      if (object == null) {
         return "";
      } else {
         YamlConfiguration yamlConfig = (YamlConfiguration)YAML_CONFIG.get();

         String var3;
         try {
            yamlConfig.set(key, object);
            var3 = yamlConfig.saveToString();
         } finally {
            yamlConfig.set(key, (Object)null);
         }

         return var3;
      }
   }

   public static String toConfigYamlWithoutTrailingNewline(String key, Object object) {
      return StringUtils.stripTrailingNewlines(toConfigYaml(key, object));
   }

   @Nullable
   public static <T> T fromConfigYaml(@Nullable String yamlConfigString, String key) {
      if (StringUtils.isEmpty(yamlConfigString)) {
         return null;
      } else {
         assert yamlConfigString != null;

         YamlConfiguration yamlConfig = (YamlConfiguration)YAML_CONFIG.get();

         Object var4;
         try {
            yamlConfig.loadFromString(yamlConfigString);
            Object var3 = yamlConfig.get(key);
            return var3;
         } catch (InvalidConfigurationException var8) {
            var4 = null;
         } finally {
            clearConfigSection(yamlConfig);
         }

         return var4;
      }
   }

   public static void loadConfigSafely(FileConfiguration config, String contents) throws InvalidConfigurationException {
      Validate.notNull(config, (String)"config is null");
      Logger configSerializationLogger = Logger.getLogger(ConfigurationSerialization.class.getName());
      Handler[] handlers = configSerializationLogger.getHandlers();
      boolean useParent = configSerializationLogger.getUseParentHandlers();
      boolean var15 = false;

      Handler[] var5;
      int var6;
      int var7;
      Handler handler;
      try {
         var15 = true;
         var5 = handlers;
         var6 = handlers.length;

         for(var7 = 0; var7 < var6; ++var7) {
            handler = var5[var7];
            configSerializationLogger.removeHandler(handler);
         }

         configSerializationLogger.setUseParentHandlers(false);
         configSerializationLogger.addHandler(ERROR_DETECTION_HANDLER);
         config.loadFromString(contents);
         LogRecord error = ERROR_DETECTION_HANDLER.getLastLogRecord();
         if (error != null) {
            throw new InvalidConfigurationException(error.getMessage(), error.getThrown());
         }

         var15 = false;
      } finally {
         if (var15) {
            ERROR_DETECTION_HANDLER.reset();
            configSerializationLogger.removeHandler(ERROR_DETECTION_HANDLER);
            Handler[] var10 = handlers;
            int var11 = handlers.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               Handler handler = var10[var12];
               configSerializationLogger.addHandler(handler);
            }

            configSerializationLogger.setUseParentHandlers(useParent);
         }
      }

      ERROR_DETECTION_HANDLER.reset();
      configSerializationLogger.removeHandler(ERROR_DETECTION_HANDLER);
      var5 = handlers;
      var6 = handlers.length;

      for(var7 = 0; var7 < var6; ++var7) {
         handler = var5[var7];
         configSerializationLogger.addHandler(handler);
      }

      configSerializationLogger.setUseParentHandlers(useParent);
   }

   private ConfigUtils() {
   }

   static {
      ERROR_DETECTION_HANDLER.setLevel(Level.SEVERE);
   }
}
