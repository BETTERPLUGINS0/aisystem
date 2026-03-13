package com.volmit.iris.util.slimjar.util;

import com.volmit.iris.util.slimjar.resolver.data.Repository;
import org.jetbrains.annotations.NotNull;

public final class Repositories {
   private Repositories() {
   }

   @NotNull
   public static String fetchFormattedUrl(@NotNull Repository var0) {
      String var1 = var0.url().toString();
      if (!var1.endsWith("/")) {
         var1 = var1 + "/";
      }

      return var1;
   }
}
