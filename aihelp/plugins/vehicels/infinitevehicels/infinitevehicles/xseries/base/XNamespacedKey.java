package me.PM2.infinitevehicles.xseries.base;

import com.google.common.base.Preconditions;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

class XNamespacedKey {
   private static final boolean SUPPORTS_NamespacedKey_fromString;

   private static boolean isValidNamespaceChar(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= '0' && var0 <= '9' || var0 == '.' || var0 == '_' || var0 == '-';
   }

   private static boolean isValidKeyChar(char var0) {
      return isValidNamespaceChar(var0) || var0 == '/';
   }

   private static boolean isValidNamespace(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1; ++var2) {
            if (!isValidNamespaceChar(var0.charAt(var2))) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean isValidKey(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1; ++var2) {
            if (!isValidKeyChar(var0.charAt(var2))) {
               return false;
            }
         }

         return true;
      }
   }

   protected static NamespacedKey fromString(@NotNull String var0) {
      if (SUPPORTS_NamespacedKey_fromString) {
         return NamespacedKey.fromString(var0);
      } else {
         Preconditions.checkArgument(var0 != null && !var0.isEmpty(), "Input string must not be empty or null");
         String[] var1 = var0.split(":", 3);
         if (var1.length > 2) {
            return null;
         } else {
            String var2 = var1.length == 2 ? var1[1] : "";
            String var3;
            if (var1.length == 1) {
               var3 = var1[0];
               return !var3.isEmpty() && isValidKey(var3) ? NamespacedKey.minecraft(var3) : null;
            } else if (var1.length == 2 && !isValidKey(var2)) {
               return null;
            } else {
               var3 = var1[0];
               if (var3.isEmpty()) {
                  return NamespacedKey.minecraft(var2);
               } else {
                  return !isValidNamespace(var3) ? null : new NamespacedKey(var3, var2);
               }
            }
         }
      }
   }

   static {
      boolean var0;
      try {
         Class var1 = Class.forName("org.bukkit.NamespacedKey");
         var1.getDeclaredMethod("fromString", String.class);
         var0 = true;
      } catch (Throwable var2) {
         var0 = false;
      }

      SUPPORTS_NamespacedKey_fromString = var0;
   }
}
