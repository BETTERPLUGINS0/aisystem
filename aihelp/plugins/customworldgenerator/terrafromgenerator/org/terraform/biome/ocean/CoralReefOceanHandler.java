package org.terraform.biome.ocean;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.tree.TreeDB;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;

public class CoralReefOceanHandler extends AbstractOceanHandler {
   public CoralReefOceanHandler(BiomeType oceanType) {
      super(oceanType);
   }

   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return this.oceanType == BiomeType.DEEP_OCEANIC ? Biome.DEEP_LUKEWARM_OCEAN : Biome.LUKEWARM_OCEAN;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.SAND, Material.SAND, (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.SAND, Material.SAND), (Material)GenUtils.randChoice(rand, Material.STONE), (Material)GenUtils.randChoice(rand, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY >= TerraformGenerator.seaLevel - 2) {
         data.setType(rawX, surfaceY, rawZ, Material.SAND);
      } else if (surfaceY >= TerraformGenerator.seaLevel - 4 && random.nextBoolean()) {
         data.setType(rawX, surfaceY, rawZ, Material.SAND);
      }

      if (BlockUtils.isStoneLike(data.getType(rawX, surfaceY, rawZ))) {
         if (GenUtils.chance(random, 10, 100)) {
            CoralGenerator.generateKelpGrowth(data, rawX, surfaceY + 1, rawZ);
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] largeCorals = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 25, 0.4F);
      SimpleLocation[] smallCorals = largeCorals;
      int var6 = largeCorals.length;

      int var7;
      int coralY;
      for(var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = smallCorals[var7];
         coralY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         if (coralY < TerraformGenerator.seaLevel) {
            sLoc = sLoc.getAtY(coralY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
               TreeDB.spawnRandomGiantCoral(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
               if (data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.GRAVEL) {
                  BlockUtils.replaceCircularPatch(random.nextInt(9999), 4.0F, new SimpleBlock(data, sLoc), Material.SAND);
               }
            }
         }
      }

      smallCorals = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 4, 0.5F);
      SimpleLocation[] rocks = smallCorals;
      var7 = smallCorals.length;

      int var14;
      for(var14 = 0; var14 < var7; ++var14) {
         SimpleLocation sLoc = rocks[var14];
         int coralY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         if (coralY < TerraformGenerator.seaLevel) {
            sLoc = sLoc.getAtY(coralY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && !data.getType(sLoc.getX(), sLoc.getY() + 1, sLoc.getZ()).isSolid()) {
               CoralGenerator.generateCoral(data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
               if (data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.GRAVEL) {
                  BlockUtils.replaceCircularPatch(random.nextInt(9999), 2.0F, new SimpleBlock(data, sLoc), Material.SAND);
               }
            }
         }
      }

      rocks = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 30, 0.4F);
      SimpleLocation[] var13 = rocks;
      var14 = rocks.length;

      for(coralY = 0; coralY < var14; ++coralY) {
         SimpleLocation sLoc = var13[coralY];
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
            int rockY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(rockY);
            if (data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.GRAVEL) {
               BlockUtils.replaceSphere(random.nextInt(9987), (float)GenUtils.randDouble(random, 3.0D, 7.0D), (float)GenUtils.randDouble(random, 2.0D, 4.0D), (float)GenUtils.randDouble(random, 3.0D, 7.0D), new SimpleBlock(data, sLoc), true, (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.GRANITE, Material.ANDESITE, Material.DIORITE)));
            }
         }
      }

   }
}
