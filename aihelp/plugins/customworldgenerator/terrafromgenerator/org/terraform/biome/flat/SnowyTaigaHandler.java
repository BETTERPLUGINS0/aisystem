package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Snowable;
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

public class SnowyTaigaHandler extends BiomeHandler {
   public static void defrostAndReplacePodzol(int seed, float radius, @NotNull SimpleBlock base) {
      if (!(radius <= 0.0F)) {
         if ((double)radius <= 0.5D) {
            base.setType((Material)GenUtils.randChoice(new Random((long)seed), Material.PODZOL));
         } else {
            FastNoise noise = new FastNoise(seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.13F);
            Random rand = new Random((long)seed);

            for(float x = -radius; x <= radius; ++x) {
               for(float z = -radius; z <= radius; ++z) {
                  SimpleBlock rel = base.getRelative(Math.round(x), 0, Math.round(z));
                  rel = rel.getGround();
                  if (BlockUtils.isDirtLike(rel.getType())) {
                     double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)radius, 2.0D);
                     double noiseVal = (double)Math.abs(noise.GetNoise((float)rel.getX(), (float)rel.getZ()));
                     if (equationResult <= 1.0D + noiseVal) {
                        if (equationResult * 4.0D > 0.7D + noiseVal) {
                           if (rand.nextBoolean()) {
                              rel.setType(Material.PODZOL);
                              rel.getUp().lsetType(Material.AIR);
                           }
                        } else {
                           rel.setType(Material.PODZOL);
                           rel.getUp().lsetType(Material.AIR);
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.SNOWY_TAIGA;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{GenUtils.weightedRandomMaterial(rand, Material.GRASS_BLOCK, 35, Material.DIRT, 3, Material.PODZOL, 2), Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (data.getType(rawX, surfaceY, rawZ) == Material.DIRT && GenUtils.chance(random, 1, 20)) {
         PlantBuilder.DEAD_BUSH.build(data, rawX, surfaceY + 1, rawZ);
         if (random.nextBoolean()) {
            PlantBuilder.ALLIUM.build(data, rawX, surfaceY + 1, rawZ);
         }
      }

      if (data.getType(rawX, surfaceY + 1, rawZ) == Material.AIR && GenUtils.isGroundLike(data.getType(rawX, surfaceY, rawZ))) {
         data.setType(rawX, surfaceY + 1, rawZ, Material.SNOW);
         BlockData var8 = data.getBlockData(rawX, surfaceY, rawZ);
         if (var8 instanceof Snowable) {
            Snowable snowable = (Snowable)var8;
            snowable.setSnowy(true);
            data.setBlockData(rawX, surfaceY, rawZ, snowable);
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
               FractalTypes.Tree.TAIGA_BIG.build(tw, new SimpleBlock(data, sLoc.getX(), sLoc.getY(), sLoc.getZ()), (b) -> {
                  b.getFractalLeaves().setSnowy(true);
               });
               defrostAndReplacePodzol(tw.getHashedRand((long)sLoc.getX(), sLoc.getY(), sLoc.getZ()).nextInt(9999), 2.5F, new SimpleBlock(data, sLoc.getX(), sLoc.getY() - 1, sLoc.getZ()));
            } else {
               FractalTypes.Tree.TAIGA_SMALL.build(tw, new SimpleBlock(data, sLoc.getX(), sLoc.getY(), sLoc.getZ()), (b) -> {
                  b.getFractalLeaves().setSnowy(true);
               });
               defrostAndReplacePodzol(tw.getHashedRand((long)sLoc.getX(), sLoc.getY(), sLoc.getZ()).nextInt(9999), 1.5F, new SimpleBlock(data, sLoc.getX(), sLoc.getY() - 1, sLoc.getZ()));
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.ICY_BEACH;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.FROZEN_RIVER;
   }
}
