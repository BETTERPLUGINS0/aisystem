package com.nisovin.shopkeepers.api.internal;

import com.google.common.base.Preconditions;
import org.checkerframework.checker.nullness.qual.Nullable;

public class InternalShopkeepersAPI {
   @Nullable
   private static InternalShopkeepersPlugin plugin = null;

   public static void enable(InternalShopkeepersPlugin plugin) {
      Preconditions.checkNotNull(plugin, "plugin is null");
      if (InternalShopkeepersAPI.plugin != null) {
         throw new IllegalStateException("API is already enabled!");
      } else {
         InternalShopkeepersAPI.plugin = plugin;
      }
   }

   public static void disable() {
      if (plugin == null) {
         throw new IllegalStateException("API is already disabled!");
      } else {
         plugin = null;
      }
   }

   public static boolean isEnabled() {
      return plugin != null;
   }

   public static InternalShopkeepersPlugin getPlugin() {
      InternalShopkeepersPlugin plugin = InternalShopkeepersAPI.plugin;
      if (plugin == null) {
         throw new IllegalStateException("API is not enabled!");
      } else {
         return plugin;
      }
   }

   private InternalShopkeepersAPI() {
   }
}
