package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.compat.MC_1_21_11;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;

public enum NautilusArmor {
   COPPER(MC_1_21_11.COPPER_NAUTILUS_ARMOR),
   GOLDEN(MC_1_21_11.GOLDEN_NAUTILUS_ARMOR),
   IRON(MC_1_21_11.IRON_NAUTILUS_ARMOR),
   DIAMOND(MC_1_21_11.DIAMOND_NAUTILUS_ARMOR),
   NETHERITE(MC_1_21_11.NETHERITE_NAUTILUS_ARMOR);

   @Nullable
   private final Material material;

   private NautilusArmor(@Nullable Material param3) {
      this.material = material;
   }

   @Nullable
   public Material getMaterial() {
      return this.material;
   }

   public boolean isEnabled() {
      return this.material != null;
   }

   // $FF: synthetic method
   private static NautilusArmor[] $values() {
      return new NautilusArmor[]{COPPER, GOLDEN, IRON, DIAMOND, NETHERITE};
   }
}
