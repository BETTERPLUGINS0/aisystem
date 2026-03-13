package org.terraform.biome.cavepopulators;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.populatordata.PopulatorDataICAAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICABiomeWriterAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.MultipleFacingBuilder;
import org.terraform.utils.version.V_1_19;

public class DeepDarkClusterCavePopulator extends AbstractCaveClusterPopulator {
   public DeepDarkClusterCavePopulator(float radius) {
      super(radius);
   }

   public static void oneUnit(TerraformWorld tw, @NotNull Random random, @NotNull SimpleBlock origin) {
      if (!origin.isSolid()) {
         (new DeepDarkClusterCavePopulator(0.0F)).oneUnit(tw, random, origin.findStonelikeCeiling(50), origin.findStonelikeFloor(50), false);
         origin.setType(Material.GLASS);
      }
   }

   public void oneUnit(TerraformWorld tw, @NotNull Random random, @Nullable SimpleBlock ceil, @Nullable SimpleBlock floor, boolean boundary) {
      if (ceil != null && floor != null) {
         if (ceil.getType() != V_1_19.SCULK && floor.getType() != V_1_19.SCULK) {
            MultipleFacing sculkVein;
            if (!BlockUtils.isWet(ceil.getDown())) {
               if (Tag.SLABS.isTagged(floor.getType()) || Tag.WALLS.isTagged(floor.getType())) {
                  return;
               }

               if (BlockUtils.isStoneLike(ceil.getType())) {
                  ceil.setType(V_1_19.SCULK);
               } else {
                  sculkVein = (MultipleFacing)Bukkit.createBlockData(V_1_19.SCULK_VEIN);
                  sculkVein.setFace(BlockFace.UP, true);
                  ceil.getDown().setBlockData(sculkVein);
               }
            }

            if (!BlockUtils.isWet(floor.getUp())) {
               if (BlockUtils.isStoneLike(ceil.getType())) {
                  floor.setType(V_1_19.SCULK);
               } else {
                  sculkVein = (MultipleFacing)Bukkit.createBlockData(V_1_19.SCULK_VEIN);
                  sculkVein.setFace(BlockFace.DOWN, true);
                  floor.getUp().setBlockData(sculkVein);
               }

               if (GenUtils.chance(random, 1, 20)) {
                  floor.getUp().setType(V_1_19.SCULK_CATALYST);
               } else if (GenUtils.chance(random, 1, 17)) {
                  floor.getUp().setType(V_1_19.SCULK_SENSOR);
               } else if (GenUtils.chance(random, 1, 25)) {
                  floor.getUp().setType(V_1_19.SCULK_SHRIEKER);
               }

               for(SimpleBlock target = floor; target.getY() != ceil.getY(); target = target.getUp()) {
                  BlockFace[] var7 = BlockUtils.directBlockFaces;
                  int var8 = var7.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     BlockFace face = var7[var9];
                     SimpleBlock rel = target.getRelative(face);
                     if (BlockUtils.isStoneLike(rel.getType())) {
                        rel.setType(V_1_19.SCULK);
                        if (BlockUtils.isAir(target.getType()) && GenUtils.chance(random, 1, 5)) {
                           (new MultipleFacingBuilder(V_1_19.SCULK_VEIN)).setFace(face, true).apply(target);
                        }
                     }
                  }
               }

               PopulatorDataICAAbstract var14 = TerraformGeneratorPlugin.injector.getICAData(ceil.getPopData());
               if (var14 instanceof PopulatorDataICABiomeWriterAbstract) {
                  for(PopulatorDataICABiomeWriterAbstract data = (PopulatorDataICABiomeWriterAbstract)var14; floor.getY() < ceil.getY(); floor = floor.getUp()) {
                     data.setBiome(floor.getX(), floor.getY(), floor.getZ(), V_1_19.DEEP_DARK);
                  }
               }

            }
         }
      }
   }
}
