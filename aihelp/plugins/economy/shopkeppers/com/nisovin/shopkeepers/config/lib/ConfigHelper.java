package com.nisovin.shopkeepers.config.lib;

import java.util.Locale;
import java.util.regex.Pattern;

public final class ConfigHelper {
   private static final Pattern CONFIG_KEY_PATTERN = Pattern.compile("([A-Z])");

   public static String toConfigKey(String fieldName) {
      String configKey = CONFIG_KEY_PATTERN.matcher(fieldName).replaceAll("-$1");
      configKey = configKey.toLowerCase(Locale.ROOT);
      configKey = configKey.replace('_', '.');
      configKey = configKey.replace("..", "_");
      return configKey;
   }

   private ConfigHelper() {
   }
}
