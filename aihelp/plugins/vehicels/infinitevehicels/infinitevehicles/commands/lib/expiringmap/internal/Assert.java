package me.PM2.infinitevehicles.commands.lib.expiringmap.internal;

import java.util.NoSuchElementException;

public final class Assert {
   private Assert() {
   }

   public static <T> T notNull(T var0, String var1) {
      if (var0 == null) {
         throw new NullPointerException(var1 + " cannot be null");
      } else {
         return var0;
      }
   }

   public static void operation(boolean var0, String var1) {
      if (!var0) {
         throw new UnsupportedOperationException(var1);
      }
   }

   public static void state(boolean var0, String var1, Object... var2) {
      if (!var0) {
         throw new IllegalStateException(String.format(var1, var2));
      }
   }

   public static void element(Object var0, Object var1) {
      if (var0 == null) {
         throw new NoSuchElementException(var1.toString());
      }
   }
}
