package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.Ageable;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class TaigaHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.TAIGA;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      FastNoise sweetBerriesNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_TAIGA_BERRY_BUSHNOISE, (w) -> {
         FastNoise n = new FastNoise((int)(w.getSeed() * 2L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFrequency(0.04F);
         return n;
      });
      if (BlockUtils.isDirtLike(data.getType(rawX, surfaceY, rawZ)) && data.getType(rawX, surfaceY + 1, rawZ) == Material.AIR) {
         if (TConfig.arePlantsEnabled() && (double)sweetBerriesNoise.GetNoise((float)rawX, (float)rawZ) > 0.3D && (double)(sweetBerriesNoise.GetNoise((float)rawX, (float)rawZ) * random.nextFloat()) > 0.35D) {
            Ageable bush = (Ageable)Material.SWEET_BERRY_BUSH.createBlockData();
            bush.setAge(GenUtils.randInt(random, 1, 3));
            data.setBlockData(rawX, surfaceY + 1, rawZ, bush);
            return;
         }

         if (GenUtils.chance(random, 1, 16)) {
            int i = random.nextInt(4);
            if (i >= 2) {
               if (random.nextBoolean()) {
                  PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
               } else {
                  PlantBuilder.LARGE_FERN.build(data, rawX, surfaceY + 1, rawZ);
               }
            } else if (i == 1) {
               if (random.nextBoolean()) {
                  PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
               } else {
                  PlantBuilder.FERN.build(data, rawX, surfaceY + 1, rawZ);
               }
            } else {
               BlockUtils.pickFlower().build(data, rawX, surfaceY + 1, rawZ);
            }
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 11);
      SimpleLocation[] var5 = trees;
      int var6 = trees.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
            int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (TConfig.c.TREES_TAIGA_BIG_ENABLED && GenUtils.chance(random, 1, 20)) {
               FractalTypes.Tree.TAIGA_BIG.build(tw, new SimpleBlock(data, sLoc.getX(), sLoc.getY(), sLoc.getZ()));
            } else {
               FractalTypes.Tree.TAIGA_SMALL.build(tw, new SimpleBlock(data, sLoc.getX(), sLoc.getY(), sLoc.getZ()));
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.ROCKY_BEACH;
   }
}
