package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Locale;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class NamespacedKeyUtils {
   public static final char NAMESPACED_KEY_SEPARATOR_CHAR = ':';
   public static final String NAMESPACED_KEY_SEPARATOR = String.valueOf(':');
   public static final String MINECRAFT_NAMESPACE_PREFIX;

   public static NamespacedKey create(String namespace, String key) {
      return new NamespacedKey(namespace, key);
   }

   @Nullable
   public static NamespacedKey parse(String input) {
      Validate.notNull(input, (String)"input is null");
      if (input.isEmpty()) {
         return null;
      } else {
         String normalizedInput = normalizeNamespacedKey(input);
         String[] components = normalizedInput.split(NAMESPACED_KEY_SEPARATOR, 3);
         if (components.length > 2) {
            return null;
         } else {
            String namespace;
            String key;
            if (components.length == 2) {
               namespace = components[0];
               key = components[1];
            } else {
               assert components.length == 1;

               namespace = "";
               key = components[0];
            }

            assert namespace != null && key != null;

            if (namespace.isEmpty()) {
               namespace = "minecraft";
            }

            if (key.isEmpty()) {
               return null;
            } else {
               assert !namespace.isEmpty() && !key.isEmpty();

               if (namespace.equals("minecraft")) {
                  key = key.replace('-', '_');
               }

               try {
                  NamespacedKey namespacedKey = create(namespace, key);
                  return namespacedKey;
               } catch (IllegalArgumentException var7) {
                  return null;
               }
            }
         }
      }
   }

   public static String normalizeNamespacedKey(String namespacedKey) {
      Validate.notNull(namespacedKey, (String)"namespacedKey is null");
      String normalized = namespacedKey.trim();
      normalized = StringUtils.replaceWhitespace(normalized, "_");
      normalized = normalized.toLowerCase(Locale.ROOT);
      return normalized;
   }

   public static String normalizeMinecraftNamespacedKey(String namespacedKey) {
      Validate.notNull(namespacedKey, (String)"namespacedKey is null");
      String normalized = normalizeNamespacedKey(namespacedKey);
      normalized = normalized.replace('-', '_');
      return normalized;
   }

   private NamespacedKeyUtils() {
   }

   static {
      MINECRAFT_NAMESPACE_PREFIX = "minecraft" + NAMESPACED_KEY_SEPARATOR;
   }
}
