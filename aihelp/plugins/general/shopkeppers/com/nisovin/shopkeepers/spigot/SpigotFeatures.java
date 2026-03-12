package com.nisovin.shopkeepers.spigot;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class SpigotFeatures {
   @Nullable
   private static Boolean SPIGOT_AVAILABLE = null;

   public static boolean isSpigotAvailable() {
      if (SPIGOT_AVAILABLE == null) {
         try {
            Class.forName("org.bukkit.Server$Spigot");
            SPIGOT_AVAILABLE = true;
         } catch (ClassNotFoundException var1) {
            SPIGOT_AVAILABLE = false;
         }
      }

      assert SPIGOT_AVAILABLE != null;

      return SPIGOT_AVAILABLE;
   }

   private SpigotFeatures() {
   }
}
