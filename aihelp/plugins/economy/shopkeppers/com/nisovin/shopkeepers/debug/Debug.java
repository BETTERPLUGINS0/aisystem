package com.nisovin.shopkeepers.debug;

import com.nisovin.shopkeepers.config.Settings;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Debug {
   public static boolean isDebugging() {
      return isDebugging((String)null);
   }

   public static boolean isDebugging(@Nullable String option) {
      Settings.AsyncSettings settings = Settings.async();
      return settings.debug && (option == null || settings.debugOptions.contains(option));
   }

   private Debug() {
   }
}
