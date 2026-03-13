package com.nisovin.shopkeepers.compat;

import com.nisovin.shopkeepers.util.java.ClassUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Optional;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class MC_1_21_11 {
   private static Optional<Boolean> IS_AVAILABLE = Optional.empty();
   @Nullable
   public static final Material NETHERITE_HORSE_ARMOR = CompatUtils.getMaterial("NETHERITE_HORSE_ARMOR");
   @Nullable
   public static final Material COPPER_NAUTILUS_ARMOR = CompatUtils.getMaterial("COPPER_NAUTILUS_ARMOR");
   @Nullable
   public static final Material GOLDEN_NAUTILUS_ARMOR = CompatUtils.getMaterial("GOLDEN_NAUTILUS_ARMOR");
   @Nullable
   public static final Material IRON_NAUTILUS_ARMOR = CompatUtils.getMaterial("IRON_NAUTILUS_ARMOR");
   @Nullable
   public static final Material DIAMOND_NAUTILUS_ARMOR = CompatUtils.getMaterial("DIAMOND_NAUTILUS_ARMOR");
   @Nullable
   public static final Material NETHERITE_NAUTILUS_ARMOR = CompatUtils.getMaterial("NETHERITE_NAUTILUS_ARMOR");

   public static void init() {
      if (isAvailable()) {
         Log.debug("MC 1.21.11 exclusive features are enabled.");
      } else {
         Log.debug("MC 1.21.11 exclusive features are disabled.");
      }

   }

   public static boolean isAvailable() {
      if (!IS_AVAILABLE.isPresent()) {
         boolean isAvailable = ClassUtils.getClassOrNull("org.bukkit.entity.ZombieNautilus") != null;
         IS_AVAILABLE = Optional.of(isAvailable);
      }

      return (Boolean)IS_AVAILABLE.get();
   }

   private MC_1_21_11() {
   }
}
