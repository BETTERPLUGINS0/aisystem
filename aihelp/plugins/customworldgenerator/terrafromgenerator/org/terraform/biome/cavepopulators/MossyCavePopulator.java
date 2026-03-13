package org.terraform.biome.cavepopulators;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.StalactiteBuilder;

public class MossyCavePopulator extends AbstractCavePopulator {
   public void populate(TerraformWorld tw, @NotNull Random random, @NotNull SimpleBlock ceil, @NotNull SimpleBlock floor) {
      int caveHeight = ceil.getY() - floor.getY();
      if (!Tag.SLABS.isTagged(floor.getType()) && !Tag.WALLS.isTagged(floor.getType())) {
         Wall w;
         if (GenUtils.chance(random, 1, 35)) {
            w = new Wall(ceil);
            if (w.getUp().getType() != Material.SAND && w.getUp().getType() != Material.SANDSTONE) {
               if (w.getUp().getType() == Material.DEEPSLATE) {
                  (new StalactiteBuilder(new Material[]{Material.COBBLED_DEEPSLATE_WALL})).setSolidBlockType(Material.DEEPSLATE).setFacingUp(false).setVerticalSpace(caveHeight).build(random, w);
               } else if (BlockUtils.isStoneLike(w.getUp().getType())) {
                  (new StalactiteBuilder(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL})).setSolidBlockType(Material.COBBLESTONE, Material.MOSSY_COBBLESTONE).setFacingUp(false).setVerticalSpace(caveHeight).build(random, w);
               }
            } else {
               (new StalactiteBuilder(new Material[]{Material.SANDSTONE_WALL})).setSolidBlockType(Material.SANDSTONE).setFacingUp(false).setVerticalSpace(caveHeight).build(random, w);
            }
         }

         if (GenUtils.chance(random, 1, 35)) {
            w = new Wall(floor.getUp(), BlockFace.NORTH);
            if (BlockUtils.isAir(w.getType())) {
               if (floor.getType() != Material.SAND && floor.getType() != Material.SANDSTONE) {
                  if (floor.getType() == Material.DEEPSLATE) {
                     (new StalactiteBuilder(new Material[]{Material.COBBLED_DEEPSLATE_WALL})).setSolidBlockType(Material.DEEPSLATE).setFacingUp(true).setVerticalSpace(caveHeight).build(random, w);
                  } else if (BlockUtils.isStoneLike(floor.getType())) {
                     (new StalactiteBuilder(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL})).setSolidBlockType(Material.COBBLESTONE, Material.MOSSY_COBBLESTONE).setFacingUp(true).setVerticalSpace(caveHeight).build(random, w);
                  }
               } else {
                  (new StalactiteBuilder(new Material[]{Material.SANDSTONE_WALL})).setSolidBlockType(Material.SANDSTONE).setFacingUp(true).setVerticalSpace(caveHeight).build(random, w);
               }
            }
         } else if (GenUtils.chance(random, 1, 25) && BlockUtils.isStoneLike(floor.getUp().getType())) {
            SimpleBlock base = floor.getUp();
            if (BlockUtils.isAir(base.getType())) {
               BlockFace[] var7 = BlockUtils.directBlockFaces;
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  BlockFace face = var7[var9];
                  if (base.getRelative(face).isSolid()) {
                     if (base.getDown().getType() == Material.DEEPSLATE) {
                        base.setType(Material.COBBLED_DEEPSLATE_SLAB);
                     } else {
                        base.setType(Material.STONE_SLAB);
                     }
                     break;
                  }
               }
            }
         } else if (GenUtils.chance(random, 1, 35) && BlockUtils.isStoneLike(floor.getUp().getType()) && BlockUtils.isAir(floor.getUp().getType())) {
            PlantBuilder.build(floor.getUp(), PlantBuilder.RED_MUSHROOM, PlantBuilder.BROWN_MUSHROOM);
         }

      }
   }
}
