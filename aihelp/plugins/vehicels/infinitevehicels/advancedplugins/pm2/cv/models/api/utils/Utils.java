package advancedplugins.pm2.cv.models.api.utils;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class Utils {
   public static final DecimalFormat FORMATTER = new DecimalFormat() {
      {
         this.setMaximumFractionDigits(1);
         this.setMinimumFractionDigits(1);
      }
   };

   @SafeVarargs
   @NotNull
   public static <T> T or(T... var0) {
      Object[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Object var4 = var1[var3];
         if (var4 != null) {
            return var4;
         }
      }

      throw new RuntimeException("All values are null");
   }

   @SafeVarargs
   @NotNull
   public static <T> T orDef(@NotNull T var0, T... var1) {
      int var2 = var1.length;
      Object[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         if (var6 != null) {
            return var6;
         }
      }

      return var0;
   }

   public static boolean isJava21OrHigher() {
      try {
         int var0 = Runtime.version().feature();
         return var0 >= 21;
      } catch (Exception var2) {
         int var1 = Integer.parseInt(System.getProperty("java.version"));
         return var1 >= 21;
      }
   }

   public static UUID generateUUIDFromString(String var0) {
      return UUID.nameUUIDFromBytes(var0.getBytes(StandardCharsets.UTF_8));
   }
}
