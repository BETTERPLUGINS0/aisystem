package org.terraform.biome.cavepopulators;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataICAAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICABiomeWriterAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.TreeDB;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.SphereBuilder;
import org.terraform.utils.blockdata.BisectedBuilder;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.MultipleFacingBuilder;

public class LushClusterCavePopulator extends AbstractCaveClusterPopulator {
   private final boolean isForLargeCave;

   public LushClusterCavePopulator(float radius, boolean isForLargeCave) {
      super(radius);
      this.isForLargeCave = isForLargeCave;
   }

   public void oneUnit(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull SimpleBlock ceil, @NotNull SimpleBlock floor, boolean boundary) {
      int caveHeight = ceil.getY() - floor.getY();
      int var9;
      SimpleBlock target;
      if (!BlockUtils.isWet(ceil.getDown())) {
         if (Tag.SLABS.isTagged(floor.getType()) || Tag.WALLS.isTagged(floor.getType())) {
            return;
         }

         int h;
         if (GenUtils.chance(random, 1, 8)) {
            ceil.setType(Material.ROOTED_DIRT);
            if (random.nextBoolean()) {
               PlantBuilder.HANGING_ROOTS.build(ceil.getDown());
            }
         } else {
            ceil.setType(Material.MOSS_BLOCK);
            BlockFace[] var7 = BlockUtils.sixBlockFaces;
            h = var7.length;

            for(var9 = 0; var9 < h; ++var9) {
               BlockFace face = var7[var9];
               if (ceil.getRelative(face).getType() == Material.LAVA) {
                  ceil.getRelative(face).setType(Material.AIR);
               }
            }

            if (GenUtils.chance(random, 1, 15)) {
               PlantBuilder.SPORE_BLOSSOM.build(ceil.getDown());
            }
         }

         if (this.isForLargeCave && GenUtils.chance(random, 1, 300)) {
            target = ceil.getGround();
            if (BlockUtils.isDirtLike(target.getType()) && !BlockUtils.isWet(target.getUp())) {
               TreeDB.spawnAzalea(random, tw, target.getPopData(), target.getX(), target.getY() + 1, target.getZ());
            }
         }

         int glowBerryChance = 5;
         if (this.isForLargeCave) {
            glowBerryChance = 7;
         }

         if (GenUtils.chance(random, 1, glowBerryChance)) {
            h = caveHeight / 4;
            if (h < 1) {
               h = 1;
            }

            if (h > 6) {
               h = 6;
            }

            BlockUtils.downLCaveVines(h, ceil.getDown());
         }
      }

      if (BlockUtils.isWet(floor.getUp())) {
         if (!this.isForLargeCave) {
            floor.setType(Material.CLAY);
         }

      } else {
         floor.setType(Material.MOSS_BLOCK);
         if (GenUtils.chance(random, 1, 15)) {
            if (random.nextBoolean()) {
               PlantBuilder.AZALEA.build(floor.getUp());
            } else {
               PlantBuilder.FLOWERING_AZALEA.build(floor.getUp());
            }
         } else if (GenUtils.chance(random, 1, 7)) {
            if (TConfig.arePlantsEnabled()) {
               if (random.nextBoolean()) {
                  (new DirectionalBuilder(Material.BIG_DRIPLEAF)).setFacing(BlockUtils.getDirectBlockFace(random)).apply(floor.getUp());
               } else {
                  (new BisectedBuilder(Material.SMALL_DRIPLEAF)).placeBoth(floor.getUp());
               }
            }
         } else if (GenUtils.chance(random, 1, 6)) {
            PlantBuilder.GRASS.build(floor.getUp());
         } else if (GenUtils.chance(random, 1, 7)) {
            PlantBuilder.MOSS_CARPET.build(floor.getUp());
         }

         if (!this.isForLargeCave) {
            for(target = floor; target.getY() != ceil.getY(); target = target.getUp()) {
               if (target.getY() - floor.getY() < 3 && GenUtils.chance(1, 700)) {
                  (new SphereBuilder(random, target, new Material[]{Material.WATER})).setSphereType(SphereBuilder.SphereType.LOWER_SEMISPHERE).setCointainmentMaterials(Material.CLAY).setRX(3.0F).setRY(2.0F).setRZ(3.0F).setDoLiquidContainment(true).setHardReplace(true).build();
               }

               BlockFace[] var16 = BlockUtils.directBlockFaces;
               var9 = var16.length;

               for(int var18 = 0; var18 < var9; ++var18) {
                  BlockFace face = var16[var18];
                  SimpleBlock rel = target.getRelative(face);
                  if (BlockUtils.isStoneLike(rel.getType())) {
                     rel.setType(Material.MOSS_BLOCK);
                     if (TConfig.arePlantsEnabled() && BlockUtils.isAir(target.getType()) && GenUtils.chance(random, 1, 5)) {
                        (new MultipleFacingBuilder(Material.GLOW_LICHEN)).setFace(face, true).apply(target);
                     }
                  }
               }
            }
         }

         PopulatorDataICAAbstract var17 = TerraformGeneratorPlugin.injector.getICAData(ceil.getPopData());
         if (var17 instanceof PopulatorDataICABiomeWriterAbstract) {
            for(PopulatorDataICABiomeWriterAbstract data = (PopulatorDataICABiomeWriterAbstract)var17; floor.getY() < ceil.getY(); floor = floor.getUp()) {
               data.setBiome(floor.getX(), floor.getY(), floor.getZ(), Biome.LUSH_CAVES);
            }
         }

      }
   }
}
