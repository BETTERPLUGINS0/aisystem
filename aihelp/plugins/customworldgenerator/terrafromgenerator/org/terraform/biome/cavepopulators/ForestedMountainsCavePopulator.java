package org.terraform.biome.cavepopulators;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.CylinderBuilder;
import org.terraform.utils.GenUtils;

public class ForestedMountainsCavePopulator extends AbstractCavePopulator {
   @NotNull
   private final MossyCavePopulator mossyCavePop = new MossyCavePopulator();

   public void populate(TerraformWorld tw, @NotNull Random random, @NotNull SimpleBlock ceil, @NotNull SimpleBlock floor) {
      if (ceil.getY() > TerraformGenerator.seaLevel && floor.getY() < TerraformGenerator.seaLevel && ceil.getAtY(TerraformGenerator.seaLevel).getType() == Material.WATER) {
         int caveHeight = ceil.getY() - TerraformGenerator.seaLevel - 1;
         if (caveHeight > 2) {
            if (GenUtils.chance(random, 1, 100)) {
               (new CylinderBuilder(random, floor.getRelative(0, (ceil.getY() - floor.getY()) / 2, 0), new Material[]{Material.STONE})).setRadius(1.5F).setRY((float)(ceil.getY() - floor.getY()) / 2.0F + 3.0F).setHardReplace(false).build();
            } else {
               int glowBerryChance = 15;
               if (GenUtils.chance(random, 1, glowBerryChance)) {
                  int h = caveHeight / 2;
                  if (h > 6) {
                     h = 6;
                  }

                  BlockUtils.downLCaveVines(h, ceil);
               }

               if (GenUtils.chance(random, 1, 30)) {
                  PlantBuilder.SPORE_BLOSSOM.build(ceil);
               }

               if (GenUtils.chance(random, 1, 50)) {
                  SimpleBlock at = ceil.getAtY(TerraformGenerator.seaLevel + 1);
                  if (!at.isSolid()) {
                     PlantBuilder.LILY_PAD.build(at);
                  }
               }

               if (!Tag.SLABS.isTagged(floor.getType()) && !Tag.WALLS.isTagged(floor.getType())) {
                  if (GenUtils.chance(random, 1, 20)) {
                     CoralGenerator.generateSeaPickles(floor.getPopData(), floor.getX(), floor.getY() + 1, floor.getZ());
                  }

               }
            }
         }
      } else {
         this.mossyCavePop.populate(tw, random, ceil, floor);
      }
   }
}
