package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.TreeDB;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;
import org.terraform.utils.version.V_1_19;

public class MangroveHandler extends BiomeHandler {
   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.MANGROVE;
   }

   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return V_1_19.MANGROVE_SWAMP;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{(Material)GenUtils.randChoice(rand, Material.GRASS_BLOCK, Material.PODZOL, Material.PODZOL), (Material)GenUtils.randChoice(rand, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   @NotNull
   public BiomeHandler getTransformHandler() {
      return this;
   }

   public void transformTerrain(@NotNull ChunkCache cache, TerraformWorld tw, @NotNull Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      int surfaceY = cache.getTransformedHeight(x, z);
      if (surfaceY < TerraformGenerator.seaLevel) {
         int rawX = chunkX * 16 + x;
         int rawZ = chunkZ * 16 + z;
         FastNoise mudNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_SWAMP_MUDNOISE, (world) -> {
            FastNoise n = new FastNoise((int)(world.getSeed() * 4L));
            n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            n.SetFrequency(0.05F);
            n.SetFractalOctaves(4);
            return n;
         });
         double noise = (double)mudNoise.GetNoise((float)rawX, (float)rawZ);
         if (noise < 0.0D) {
            noise = 0.0D;
         }

         int att = (int)Math.round(noise * 10.0D);
         if (att + surfaceY > TerraformGenerator.seaLevel) {
            att = TerraformGenerator.seaLevel - surfaceY;
         }

         for(int i = 1; i <= att; ++i) {
            if (i < att) {
               chunk.setBlock(x, surfaceY + i, z, this.getSurfaceCrust(random)[1]);
            } else {
               chunk.setBlock(x, surfaceY + i, z, this.getSurfaceCrust(random)[0]);
            }
         }

         cache.writeTransformedHeight(x, z, (short)(surfaceY + att));
      }

   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      int seaLevel = TerraformGenerator.seaLevel;
      if (BlockUtils.isStoneLike(data.getType(rawX, surfaceY, rawZ))) {
         if (surfaceY < seaLevel) {
            if (data.getType(rawX, TerraformGenerator.seaLevel, rawZ) == Material.WATER && GenUtils.chance(random, 1, 30)) {
               PlantBuilder.LILY_PAD.build(data, rawX, TerraformGenerator.seaLevel + 1, rawZ);
            }
         } else if (GenUtils.chance(random, 1, 30)) {
            PlantBuilder.FIREFLY_BUSH.build(data, rawX, surfaceY + 1, rawZ);
         }

         if (BlockUtils.isWet(new SimpleBlock(data, rawX, surfaceY + 1, rawZ)) && GenUtils.chance(random, 10, 100) && surfaceY < TerraformGenerator.seaLevel - 3) {
            CoralGenerator.generateKelpGrowth(data, rawX, surfaceY + 1, rawZ);
         }

         if (GenUtils.chance(random, TConfig.c.BIOME_CLAY_DEPOSIT_CHANCE_OUT_OF_THOUSAND, 1000)) {
            BlockUtils.generateClayDeposit(rawX, surfaceY, rawZ, data, random);
         }

         if (GenUtils.chance(random, 5, 1000)) {
            BlockUtils.replaceCircularPatch(random.nextInt(9999), 3.5F, new SimpleBlock(data, rawX, surfaceY, rawZ), V_1_19.MUD);
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      if (GenUtils.chance(random, 8, 10)) {
         int treeX = GenUtils.randInt(random, 2, 12) + data.getChunkX() * 16;
         int treeZ = GenUtils.randInt(random, 2, 12) + data.getChunkZ() * 16;
         if (data.getBiome(treeX, treeZ) == this.getBiome()) {
            int treeY = GenUtils.getHighestGround(data, treeX, treeZ);
            if (treeY > TerraformGenerator.seaLevel - 6) {
               TreeDB.spawnBreathingRoots(tw, new SimpleBlock(data, treeX, treeY, treeZ), V_1_19.MANGROVE_ROOTS);
               FractalTypes.Tree.SWAMP_TOP.build(tw, new SimpleBlock(data, treeX, treeY, treeZ), (t) -> {
                  t.setCheckGradient(false);
               });
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.MUDFLATS;
   }

   public double calculateHeight(TerraformWorld tw, int x, int z) {
      double height = HeightMap.CORE.getHeight(tw, x, z) - 10.0D;
      if (height <= 0.0D) {
         height = 3.0D;
      }

      return height;
   }
}
