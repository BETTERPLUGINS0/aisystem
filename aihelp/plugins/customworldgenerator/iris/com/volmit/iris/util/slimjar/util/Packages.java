package com.volmit.iris.util.slimjar.util;

import org.jetbrains.annotations.NotNull;

public final class Packages {
   private Packages() {
   }

   @NotNull
   public static String fix(@NotNull String var0) {
      return var0.replace('#', '.');
   }
}
