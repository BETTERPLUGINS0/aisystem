package com.nisovin.shopkeepers.compat;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.nullness.qual.Nullable;

final class CompatUtils {
   @Nullable
   public static EntityType getEntityType(String name) {
      try {
         return EntityType.valueOf(name);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   @Nullable
   public static Material getMaterial(String name) {
      try {
         return Material.valueOf(name);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   private CompatUtils() {
   }
}
