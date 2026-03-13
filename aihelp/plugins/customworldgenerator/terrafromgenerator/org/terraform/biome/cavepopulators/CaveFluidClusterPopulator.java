package org.terraform.biome.cavepopulators;

import java.util.Random;
import java.util.Set;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class CaveFluidClusterPopulator extends AbstractCaveClusterPopulator {
   Random rand;
   @Nullable
   Material fluid;
   int rY;

   public CaveFluidClusterPopulator(float radius) {
      super(radius);
   }

   public void oneUnit(@NotNull TerraformWorld tw, Random doNotUse, @Nullable SimpleBlock ceil, @Nullable SimpleBlock floor, boolean boundary) {
      if (ceil != null && floor != null) {
         if (this.rand == null) {
            this.rand = tw.getHashedRand((long)this.center.getX(), this.center.getY(), this.center.getZ());
            this.fluid = (Material)GenUtils.choice(this.rand, new Material[]{Material.WATER, Material.LAVA});
            if (this.center.getY() < TerraformGeneratorPlugin.injector.getMinY() + 32) {
               this.fluid = Material.LAVA;
            }

            this.rY = 3 + this.rand.nextInt(3);
         }

         Material original = floor.getType();

         for(int i = 0; i < this.rY; ++i) {
            if (boundary) {
               floor.setType(original);
            } else if (floor.getY() <= this.lowestYCenter.getY()) {
               floor.setType(this.fluid);
            } else if (!BlockUtils.isExposedToMaterial(floor, (Set)BlockUtils.fluids) && !BlockUtils.fluids.contains(floor.getUp().getType())) {
               floor.setType(Material.CAVE_AIR);
            }

            floor = floor.getDown();
            if (!floor.isSolid()) {
               floor.setType(original);
               break;
            }
         }

      }
   }
}
