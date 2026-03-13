package org.terraform.structure.mineshaft;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class BadlandsMineshaftPathPopulator extends MineshaftPathPopulator {
   public BadlandsMineshaftPathPopulator(Random rand) {
      super(rand);
   }

   @NotNull
   public Material[] getPathMaterial() {
      return new Material[]{Material.DARK_OAK_PLANKS, Material.DARK_OAK_SLAB, Material.DARK_OAK_PLANKS, Material.DARK_OAK_SLAB, Material.GRAVEL};
   }

   @NotNull
   public Material getFenceMaterial() {
      return Material.DARK_OAK_FENCE;
   }

   @NotNull
   public Material getSupportMaterial() {
      return Material.DARK_OAK_LOG;
   }
}
