package org.terraform.biome.cavepopulators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.CoordPair;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class MasterCavePopulatorDistributor {
   private static final HashSet<Class<?>> populatedBefore = new HashSet();

   public void populate(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, boolean generateClusters) {
      HashMap<CoordPair, CaveClusterRegistry> clusters = generateClusters ? this.calculateClusterLocations(random, tw, data.getChunkX(), data.getChunkZ()) : new HashMap();
      ChunkCache cache = TerraformGenerator.getCache(tw, data.getChunkX(), data.getChunkZ());

      for(int x = data.getChunkX() * 16; x < data.getChunkX() * 16 + 16; ++x) {
         label84:
         for(int z = data.getChunkZ() * 16; z < data.getChunkZ() * 16 + 16; ++z) {
            BiomeBank bank = tw.getBiomeBank(x, z);
            int maxHeightForCaves = bank.getHandler().getMaxHeightForCaves(tw, x, z);
            CaveClusterRegistry reg = (CaveClusterRegistry)clusters.remove(new CoordPair(x, z));
            Collection<CoordPair> pairs = getCaveCeilFloors(data, x, z, 4, cache);
            int clusterPair = !pairs.isEmpty() ? random.nextInt(pairs.size()) : 0;
            Iterator var14 = pairs.iterator();

            while(true) {
               SimpleBlock ceil;
               SimpleBlock floor;
               do {
                  do {
                     do {
                        CoordPair pair;
                        do {
                           if (!var14.hasNext()) {
                              continue label84;
                           }

                           pair = (CoordPair)var14.next();
                        } while(pair.x() > maxHeightForCaves);

                        ceil = new SimpleBlock(data, x, pair.x(), z);
                        floor = new SimpleBlock(data, x, pair.z(), z);
                     } while(BlockUtils.amethysts.contains(floor.getType()));
                  } while(BlockUtils.fluids.contains(floor.getUp().getType()));
               } while(BlockUtils.amethysts.contains(ceil.getDown().getType()));

               Object pop;
               if (floor.getY() < TerraformGeneratorPlugin.injector.getMinY() + 32) {
                  pop = new DeepCavePopulator();
               } else {
                  pop = clusterPair == 0 && reg != null && TConfig.c.FEATURE_CAVECLUSTERS_ENABLED ? reg.getPopulator(random) : bank.getCavePop();
               }

               --clusterPair;
               if (!(pop instanceof AbstractCaveClusterPopulator) && !TConfig.c.FEATURE_CAVEDECORATORS_ENABLED) {
                  pop = new EmptyCavePopulator();
               }

               ((AbstractCavePopulator)pop).populate(tw, random, ceil, floor);
               if (populatedBefore.add(pop.getClass())) {
                  TLogger var10000 = TerraformGeneratorPlugin.logger;
                  String var10001 = pop.getClass().getSimpleName();
                  var10000.info("Spawning " + var10001 + " at " + String.valueOf(floor));
               }
            }
         }
      }

   }

   @NotNull
   private HashMap<CoordPair, CaveClusterRegistry> calculateClusterLocations(@NotNull Random rand, @NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      HashMap<CoordPair, CaveClusterRegistry> locs = new HashMap();
      if (!TConfig.areCavesEnabled()) {
         return locs;
      } else {
         CaveClusterRegistry[] var6 = CaveClusterRegistry.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            CaveClusterRegistry type = var6[var8];
            CoordPair[] positions = GenUtils.vectorRandomObjectPositions(tw.getHashedRand((long)chunkX, type.getHashSeed(), chunkZ).nextInt(9999999), chunkX, chunkZ, type.getSeparation(), type.getPertub());
            CoordPair[] var11 = positions;
            int var12 = positions.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               CoordPair pos = var11[var13];
               if (!locs.containsKey(pos) || !rand.nextBoolean()) {
                  locs.put(pos, type);
               }
            }
         }

         return locs;
      }
   }

   @NotNull
   public static Collection<CoordPair> getCaveCeilFloors(PopulatorDataAbstract data, int x, int z, int minimumHeight, ChunkCache cache) {
      int y = cache.getTransformedHeight(x & 15, z & 15);
      int INVAL = TerraformGeneratorPlugin.injector.getMinY() - 1;
      int[] pair = new int[]{INVAL, INVAL};
      List<CoordPair> list = new ArrayList();

      for(int ny = y - 1; ny > TerraformGeneratorPlugin.injector.getMinY(); --ny) {
         if (cache.isSolid(x & 15, ny, z & 15)) {
            pair[1] = ny;
            if (pair[0] - pair[1] >= minimumHeight) {
               list.add(new CoordPair(pair[0], pair[1]));
            }

            pair[0] = INVAL;
            pair[1] = INVAL;
         } else if (pair[0] == INVAL) {
            pair[0] = ny;
         }
      }

      return list;
   }
}
