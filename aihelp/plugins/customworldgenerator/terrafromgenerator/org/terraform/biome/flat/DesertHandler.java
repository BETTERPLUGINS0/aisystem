package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.beach.OasisBeach;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.version.V_1_21_5;
import org.terraform.utils.version.Version;

public class DesertHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.DESERT;
   }

   public BiomeHandler getTransformHandler() {
      return this;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.DESERT_RIVER;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.SAND, Material.SAND, (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.SAND), Material.SANDSTONE, Material.SANDSTONE, Material.SANDSTONE, Material.SANDSTONE, (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.STONE)};
   }

   public void populateSmallItems(@NotNull TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      boolean cactusGathering = GenUtils.chance(random, 1, 100);
      OasisBeach.generateOasisBeach(world, random, data, rawX, rawZ, BiomeBank.DESERT);
      Material base = data.getType(rawX, surfaceY, rawZ);
      if (cactusGathering && GenUtils.chance(random, 5, 100)) {
         data.setType(rawX, surfaceY, rawZ, Material.DIRT_PATH);
      }

      if (base == Material.SAND) {
         if (!GenUtils.chance(random, 1, 100) && (!GenUtils.chance(random, 1, 20) || !cactusGathering)) {
            if (GenUtils.chance(random, 1, 80)) {
               PlantBuilder.build(new SimpleBlock(data, rawX, surfaceY + 1, rawZ), PlantBuilder.DEAD_BUSH, PlantBuilder.SHORT_DRY_GRASS, PlantBuilder.TALL_DRY_GRASS);
            }
         } else {
            BlockFace[] var9 = BlockUtils.directBlockFaces;
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               BlockFace face = var9[var11];
               if (data.getType(rawX + face.getModX(), surfaceY + 1, rawZ + face.getModZ()) != Material.AIR) {
                  return;
               }
            }

            int cactusHeight = PlantBuilder.CACTUS.build(random, data, rawX, surfaceY + 1, rawZ, 3, 5);
            if (Version.VERSION.isAtLeast(Version.v1_21_5) && GenUtils.chance(random, 1, 10)) {
               data.setType(rawX, surfaceY + 1 + cactusHeight, rawZ, V_1_21_5.CACTUS_FLOWER);
            }
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] ribCages = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 256, 0.6F);
      SimpleLocation[] var5 = ribCages;
      int var6 = ribCages.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         int ribY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(ribY - GenUtils.randInt(random, 0, 6));
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && data.getType(sLoc.getX(), ribY, sLoc.getZ()) == Material.SAND) {
            this.spawnRibCage(random, new SimpleBlock(data, sLoc.getX(), sLoc.getY(), sLoc.getZ()));
         }
      }

   }

   public void spawnRibCage(@NotNull Random random, @NotNull SimpleBlock target) {
      if (TConfig.areStructuresEnabled()) {
         BlockFace direction = BlockUtils.getDirectBlockFace(random);
         int spineLength = GenUtils.randInt(random, 10, 14);
         float ribWidthRadius = (float)GenUtils.randInt(random, 1, 2) + (float)spineLength / 2.0F;
         float ribHeightRadius = 0.7F * ribWidthRadius;
         int interval = 2;
         if (random.nextBoolean()) {
            ++interval;
         }

         float ribSizeMultiplier = 1.0F;

         for(int segmentIndex = 0; segmentIndex < spineLength; ++segmentIndex) {
            Wall seg = new Wall(target.getRelative(direction, segmentIndex), direction);
            (new OrientableBuilder(Material.BONE_BLOCK)).setAxis(BlockUtils.getAxisFromBlockFace(direction)).apply(seg);
            if (segmentIndex < (int)((float)spineLength / 2.0F)) {
               ribSizeMultiplier += 0.05F;
            } else if (segmentIndex > (int)((float)spineLength / 2.0F)) {
               ribSizeMultiplier -= 0.05F;
            }

            if (segmentIndex % interval == 0 && segmentIndex > spineLength / 6) {
               for(float nHor = 1.0F; nHor <= ribWidthRadius * ribSizeMultiplier; nHor += 0.01F) {
                  int[] multipliers = new int[]{-1};
                  if (nHor > ribWidthRadius * ribSizeMultiplier / 3.0F) {
                     multipliers = new int[]{-1, 1};
                  }

                  int[] var13 = multipliers;
                  int var14 = multipliers.length;

                  for(int var15 = 0; var15 < var14; ++var15) {
                     int multiplier = var13[var15];
                     int ny = (int)Math.round((double)(ribHeightRadius * ribSizeMultiplier) + (double)((float)multiplier * ribHeightRadius * ribSizeMultiplier) * Math.sqrt(1.0D - Math.pow((double)(nHor / (ribWidthRadius * ribSizeMultiplier)), 2.0D)));
                     int horRel = Math.round(nHor);
                     Axis axis = BlockUtils.getAxisFromBlockFace(BlockUtils.getLeft(direction));
                     if ((float)ny > ribSizeMultiplier * ribHeightRadius / 3.0F && (float)ny < 5.0F * ribSizeMultiplier * ribHeightRadius / 3.0F) {
                        axis = Axis.Y;
                     }

                     (new OrientableBuilder(Material.BONE_BLOCK)).setAxis(axis).apply(seg.getRelative(0, ny, 0).getRight(horRel)).apply(seg.getRelative(0, ny, 0).getLeft(horRel));
                  }
               }
            }
         }

      }
   }
}
