package org.terraform.coregen.bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.TerraformPopulator;
import org.terraform.data.DudChunkData;
import org.terraform.data.SimpleChunkLocation;
import org.terraform.data.TWCoordPair;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.CommonMat;
import org.terraform.utils.datastructs.ConcurrentLRUCache;

public class TerraformGenerator extends ChunkGenerator {
   public static final List<SimpleChunkLocation> preWorldInitGen = new ArrayList();
   private static final DudChunkData DUD = new DudChunkData();
   public static ConcurrentLRUCache<TWCoordPair, ChunkCache> CHUNK_CACHE;
   public static int seaLevel = 62;

   public static void updateSeaLevelFromConfig() {
      seaLevel = TConfig.c.HEIGHT_MAP_SEA_LEVEL;
   }

   @NotNull
   public static ChunkCache getCache(TerraformWorld tw, int x, int z) {
      return (ChunkCache)CHUNK_CACHE.get(new TWCoordPair(tw, x, z));
   }

   public static void buildFilledCache(@NotNull TerraformWorld tw, int chunkX, int chunkZ, @NotNull ChunkCache cache) {
      Random random = tw.getHashedRand((long)chunkX, chunkZ, 31278);

      for(int x = 0; x < 16; ++x) {
         for(int z = 0; z < 16; ++z) {
            int rawX = chunkX * 16 + x;
            int rawZ = chunkZ * 16 + z;
            double preciseHeight = HeightMap.getPreciseHeight(tw, rawX, rawZ);
            cache.writeTransformedHeight(x, z, (short)((int)preciseHeight));

            for(int y = (int)preciseHeight; y >= TerraformGeneratorPlugin.injector.getMinY() && (tw.noiseCaveRegistry.canGenerateCarve(rawX, y, rawZ, preciseHeight, cache) || tw.noiseCaveRegistry.canNoiseCarve(rawX, y, rawZ, preciseHeight, cache)); --y) {
               cache.writeTransformedHeight(x, z, (short)(y - 1));
            }

            BiomeBank bank = tw.getBiomeBank(rawX, (int)preciseHeight, rawZ);
            BiomeHandler transformHandler = bank.getHandler().getTransformHandler();
            if (transformHandler != null) {
               transformHandler.transformTerrain(cache, tw, random, DUD, x, z, chunkX, chunkZ);
            }
         }
      }

   }

   public boolean isParallelCapable() {
      return true;
   }

   public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random dontCareRandom, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
      TerraformGeneratorPlugin.watchdogSuppressant.tickWatchdog();
      TerraformWorld tw = TerraformWorld.get(worldInfo.getName(), worldInfo.getSeed());
      ChunkCache cache = getCache(tw, chunkX, chunkZ);
      Random transformRandom = tw.getHashedRand((long)chunkX, chunkZ, 31278);

      for(int x = 0; x < 16; ++x) {
         for(int z = 0; z < 16; ++z) {
            int rawX = (chunkX << 4) + x;
            int rawZ = (chunkZ << 4) + z;
            double height = HeightMap.getPreciseHeight(tw, rawX, rawZ);
            cache.writeTransformedHeight(x, z, (short)((int)height));
            chunkData.setRegion(x, 3, z, x + 1, (int)height + 1, z + 1, CommonMat.STONE);
            chunkData.setRegion(x, TerraformGeneratorPlugin.injector.getMinY(), z, x + 1, 0, z + 1, CommonMat.DEEPSLATE);

            for(int y = (int)height; y >= TerraformGeneratorPlugin.injector.getMinY(); --y) {
               if (y >= 0 && y <= 2) {
                  chunkData.setBlock(x, y, z, (BlockData)GenUtils.randChoice(dontCareRandom, CommonMat.DEEPSLATE, CommonMat.STONE));
               }

               if (tw.noiseCaveRegistry.canNoiseCarve(rawX, y, rawZ, height, cache)) {
                  chunkData.setBlock(x, y, z, CommonMat.CAVE_AIR);
                  cache.cacheNonSolid(x, y, z);
               } else {
                  cache.cacheSolid(x, y, z);
               }
            }

            BiomeBank bank = tw.getBiomeBank(rawX, (int)height, rawZ);
            int index = 0;

            for(Material[] crust = bank.getHandler().getSurfaceCrust(dontCareRandom); index < crust.length; ++index) {
               chunkData.setBlock(x, (int)(height - (double)index), z, crust[index]);
            }

            chunkData.setRegion(x, (int)(height + 1.0D), z, x + 1, seaLevel + 1, z + 1, CommonMat.WATER);
            boolean mustUpdateHeight = true;

            for(int y = (int)height; y > TerraformGeneratorPlugin.injector.getMinY(); --y) {
               if (!tw.noiseCaveRegistry.canGenerateCarve(rawX, y, rawZ, height, cache) && chunkData.getType(x, y, z).isSolid()) {
                  mustUpdateHeight = false;
               } else {
                  chunkData.setBlock(x, y, z, CommonMat.CAVE_AIR);
                  cache.cacheNonSolid(x, y, z);
                  if (mustUpdateHeight) {
                     cache.writeTransformedHeight(x, z, (short)(y - 1));
                  }
               }
            }

            BiomeHandler transformHandler = bank.getHandler().getTransformHandler();
            if (transformHandler != null) {
               transformHandler.transformTerrain(cache, tw, transformRandom, chunkData, x, z, chunkX, chunkZ);
            }

            for(int i = 1; i < TConfig.c.HEIGHT_MAP_BEDROCK_HEIGHT && GenUtils.chance(dontCareRandom, TConfig.c.HEIGHT_MAP_BEDROCK_DENSITY, 100); ++i) {
               chunkData.setBlock(x, TerraformGeneratorPlugin.injector.getMinY() + i, z, CommonMat.BEDROCK);
            }
         }
      }

      chunkData.setRegion(0, TerraformGeneratorPlugin.injector.getMinY(), 0, 16, TerraformGeneratorPlugin.injector.getMinY() + 1, 16, CommonMat.BEDROCK);
   }

   public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random dontCareRandom, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
   }

   public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
   }

   public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
   }

   public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
      return new Location(world, 0.0D, (double)HeightMap.getBlockHeight(TerraformWorld.get(world), 0, 0), 0.0D);
   }

   @NotNull
   public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
      TerraformWorld tw = TerraformWorld.get(world);
      return List.of(new TerraformPopulator(), new TerraformBukkitBlockPopulator(tw));
   }

   public boolean shouldGenerateNoise() {
      return false;
   }

   public boolean shouldGenerateSurface() {
      return false;
   }

   public boolean shouldGenerateBedrock() {
      return false;
   }

   public boolean shouldGenerateCaves() {
      return false;
   }

   public boolean shouldGenerateDecorations() {
      return false;
   }

   public boolean shouldGenerateMobs() {
      return false;
   }

   public boolean shouldGenerateStructures() {
      return true;
   }
}
