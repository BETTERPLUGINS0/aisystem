package org.terraform.biome.cavepopulators;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataICAAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICABiomeWriterAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class DripstoneClusterCavePopulator extends AbstractCaveClusterPopulator {
   public DripstoneClusterCavePopulator(float radius) {
      super(radius);
   }

   protected void oneUnit(TerraformWorld tw, @NotNull Random random, @NotNull SimpleBlock ceil, @NotNull SimpleBlock floor, boolean boundary) {
      int caveHeight = ceil.getY() - floor.getY();
      if (!Tag.SLABS.isTagged(floor.getType()) && !Tag.WALLS.isTagged(floor.getType())) {
         ceil.setType(Material.DRIPSTONE_BLOCK);
         int h;
         if (GenUtils.chance(random, 1, 4)) {
            h = caveHeight / 4;
            if (h < 1) {
               h = 1;
            }

            if (h > 4) {
               h = 4;
            }

            BlockUtils.downLPointedDripstone(GenUtils.randInt(1, h), ceil.getDown());
         }

         floor.setType(Material.DRIPSTONE_BLOCK);
         if (GenUtils.chance(random, 1, 4)) {
            h = caveHeight / 4;
            if (h < 1) {
               h = 1;
            }

            if (h > 4) {
               h = 4;
            }

            BlockUtils.upLPointedDripstone(GenUtils.randInt(1, h), floor.getUp());
         }

         PopulatorDataICAAbstract var8 = TerraformGeneratorPlugin.injector.getICAData(ceil.getPopData());
         if (var8 instanceof PopulatorDataICABiomeWriterAbstract) {
            for(PopulatorDataICABiomeWriterAbstract data = (PopulatorDataICABiomeWriterAbstract)var8; floor.getY() < ceil.getY(); floor = floor.getUp()) {
               data.setBiome(floor.getX(), floor.getY(), floor.getZ(), Biome.DRIPSTONE_CAVES);
            }
         }

      }
   }
}
