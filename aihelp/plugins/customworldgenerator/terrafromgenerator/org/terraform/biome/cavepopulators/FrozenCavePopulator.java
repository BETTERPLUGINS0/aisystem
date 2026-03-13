package org.terraform.biome.cavepopulators;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.utils.GenUtils;
import org.terraform.utils.StalactiteBuilder;

public class FrozenCavePopulator extends AbstractCavePopulator {
   public void populate(TerraformWorld tw, @NotNull Random random, @NotNull SimpleBlock ceil, @NotNull SimpleBlock floor) {
      int caveHeight = ceil.getY() - floor.getY();
      if (!Tag.SLABS.isTagged(floor.getType()) && !Tag.WALLS.isTagged(floor.getType())) {
         ceil.setType(Material.ICE);
         Wall w;
         if (GenUtils.chance(random, 1, 24)) {
            w = new Wall(ceil.getDown(), BlockFace.NORTH);
            (new StalactiteBuilder(new Material[]{Material.ICE})).setSolidBlockType(Material.ICE).setFacingUp(false).setVerticalSpace(caveHeight).build(random, w);
         }

         floor.getUp().setType(Material.ICE);
         if (GenUtils.chance(random, 1, 25)) {
            w = new Wall(floor.getUp(2));
            if (w.getType() == Material.CAVE_AIR) {
               (new StalactiteBuilder(new Material[]{Material.ICE})).setSolidBlockType(Material.ICE).setFacingUp(true).setVerticalSpace(caveHeight).build(random, w);
            }
         }

      }
   }
}
