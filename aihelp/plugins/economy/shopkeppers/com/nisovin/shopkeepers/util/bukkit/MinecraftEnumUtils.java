package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.util.java.EnumUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Locale;

public final class MinecraftEnumUtils {
   private static final String MINECRAFT_NAMESPACE_PREFIX;

   public static String normalizeEnumName(String enumName) {
      Validate.notNull(enumName, (String)"enumName is null");
      String normalized = EnumUtils.normalizeEnumName(enumName);
      if (normalized.startsWith(MINECRAFT_NAMESPACE_PREFIX)) {
         normalized = normalized.substring(MINECRAFT_NAMESPACE_PREFIX.length());
      }

      return normalized;
   }

   public static <E extends Enum<E>> E parseEnum(Class<E> enumType, String enumName) {
      Validate.notNull(enumType, (String)"enumType is null");
      if (enumName != null && !enumName.isEmpty()) {
         E enumValue = EnumUtils.valueOf(enumType, enumName);
         if (enumValue != null) {
            return enumValue;
         } else {
            String normalizedEnumName = normalizeEnumName(enumName);
            return EnumUtils.valueOf(enumType, normalizedEnumName);
         }
      } else {
         return null;
      }
   }

   private MinecraftEnumUtils() {
   }

   static {
      MINECRAFT_NAMESPACE_PREFIX = NamespacedKeyUtils.MINECRAFT_NAMESPACE_PREFIX.toUpperCase(Locale.ROOT);
   }
}
