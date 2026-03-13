package org.terraform.structure;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;

public class StructureLocator {
   private static final int[] TIMEDOUT = new int[]{-7, 13};
   private static final LoadingCache<StructureLocator.StructureLocatorKey, int[]> STRUCTURELOCATION_CACHE = CacheBuilder.newBuilder().maximumSize(300L).build(new StructureLocator.StructureLocatorCacheLoader());

   public static int[] locateMultiMegaChunkStructure(TerraformWorld tw, @NotNull MegaChunk center, @NotNull MultiMegaChunkStructurePopulator populator, int timeoutMillis) {
      if (!populator.isEnabled()) {
         return null;
      } else {
         StructureLocator.StructureLocatorKey cacheKey = new StructureLocator.StructureLocatorKey(center, tw, populator);
         if (timeoutMillis != -1) {
            int[] coords = (int[])STRUCTURELOCATION_CACHE.getIfPresent(cacheKey);
            if (coords != null) {
               if (coords[0] == TIMEDOUT[0] && coords[1] == TIMEDOUT[1]) {
                  return null;
               }

               return coords;
            }
         }

         long currentTimeMillis = System.currentTimeMillis();
         int blockX = -1;
         int blockZ = -1;
         int radius = 0;

         for(boolean found = false; !found; ++radius) {
            Iterator var11 = getSurroundingChunks(center, radius).iterator();

            while(var11.hasNext()) {
               MegaChunk mc = (MegaChunk)var11.next();
               if (timeoutMillis != -1 && System.currentTimeMillis() - currentTimeMillis > (long)timeoutMillis) {
                  STRUCTURELOCATION_CACHE.put(cacheKey, TIMEDOUT);
                  break;
               }

               int[][] var13 = populator.getCoordsFromMegaChunk(tw, mc);
               int var14 = var13.length;

               for(int var15 = 0; var15 < var14; ++var15) {
                  int[] coords = var13[var15];
                  if (coords != null && TConfig.areStructuresEnabled() && populator.canSpawn(tw, coords[0] >> 4, coords[1] >> 4)) {
                     found = true;
                     blockX = coords[0];
                     blockZ = coords[1];
                     break;
                  }
               }

               if (found) {
                  break;
               }
            }
         }

         STRUCTURELOCATION_CACHE.put(cacheKey, new int[]{blockX, blockZ});
         return new int[]{blockX, blockZ};
      }
   }

   public static int[] locateSingleMegaChunkStructure(@NotNull TerraformWorld tw, int rawX, int rawZ, @NotNull SingleMegaChunkStructurePopulator populator, int timeoutMillis) {
      MegaChunk center = new MegaChunk(rawX, 0, rawZ);
      return locateSingleMegaChunkStructure(tw, center, populator, timeoutMillis);
   }

   public static int[] locateSingleMegaChunkStructure(@NotNull TerraformWorld tw, @NotNull MegaChunk center, @NotNull SingleMegaChunkStructurePopulator populator, int timeoutMillis) {
      if (!populator.isEnabled()) {
         return null;
      } else {
         StructureLocator.StructureLocatorKey cacheKey = new StructureLocator.StructureLocatorKey(center, tw, populator);
         if (timeoutMillis != -1) {
            int[] coords = (int[])STRUCTURELOCATION_CACHE.getIfPresent(cacheKey);
            if (coords != null) {
               if (coords[0] == TIMEDOUT[0] && coords[1] == TIMEDOUT[1]) {
                  return null;
               }

               return coords;
            }
         }

         long currentTimeMillis = System.currentTimeMillis();
         MegaChunk lowerBound = null;
         MegaChunk upperBound = null;
         int blockX = -1;
         int blockZ = -1;
         int radius = 0;

         label106:
         for(boolean found = false; !found; ++radius) {
            Iterator var13 = getSurroundingChunks(center, radius).iterator();

            do {
               MegaChunk mc;
               int[] coords;
               BiomeBank biome;
               do {
                  do {
                     do {
                        if (!var13.hasNext()) {
                           continue label106;
                        }

                        mc = (MegaChunk)var13.next();
                        if (timeoutMillis != -1 && System.currentTimeMillis() - currentTimeMillis > (long)timeoutMillis) {
                           STRUCTURELOCATION_CACHE.put(cacheKey, TIMEDOUT);
                           continue label106;
                        }

                        if (lowerBound == null) {
                           lowerBound = mc;
                        }

                        if (upperBound == null) {
                           upperBound = mc;
                        }

                        if (mc.getX() < lowerBound.getX() || mc.getZ() < lowerBound.getZ()) {
                           lowerBound = mc;
                        }

                        if (mc.getX() > upperBound.getX() || mc.getZ() > upperBound.getZ()) {
                           upperBound = mc;
                        }

                        coords = mc.getCenterBiomeSectionBlockCoords();
                     } while(coords == null);

                     biome = mc.getCenterBiomeSection(tw).getBiomeBank();
                  } while(!TConfig.areStructuresEnabled());
               } while(!populator.canSpawn(tw, coords[0] >> 4, coords[1] >> 4, biome));

               SingleMegaChunkStructurePopulator[] var17 = StructureRegistry.getLargeStructureForMegaChunk(tw, mc);
               int var18 = var17.length;

               for(int var19 = 0; var19 < var18; ++var19) {
                  SingleMegaChunkStructurePopulator availablePops = var17[var19];
                  if (availablePops != null && TConfig.areStructuresEnabled() && availablePops.canSpawn(tw, coords[0] >> 4, coords[1] >> 4, biome)) {
                     if (availablePops.getClass().equals(populator.getClass())) {
                        found = true;
                        blockX = coords[0];
                        blockZ = coords[1];
                     }
                     break;
                  }
               }
            } while(!found);
         }

         STRUCTURELOCATION_CACHE.put(cacheKey, new int[]{blockX, blockZ});
         return new int[]{blockX, blockZ};
      }
   }

   @NotNull
   private static Collection<MegaChunk> getSurroundingChunks(@NotNull MegaChunk center, int radius) {
      if (radius == 0) {
         return List.of(center);
      } else {
         ArrayList<MegaChunk> candidates = new ArrayList();
         int[] var3 = new int[]{-radius, radius};
         int var4 = var3.length;

         int var5;
         int rz;
         int rx;
         for(var5 = 0; var5 < var4; ++var5) {
            rz = var3[var5];

            for(rx = -radius; rx <= radius; ++rx) {
               candidates.add(center.getRelative(rz, rx));
            }
         }

         var3 = new int[]{-radius, radius};
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            rz = var3[var5];

            for(rx = 1 - radius; rx <= radius - 1; ++rx) {
               candidates.add(center.getRelative(rx, rz));
            }
         }

         return candidates;
      }
   }

   private static class StructureLocatorKey {
      private final MegaChunk mc;
      private final TerraformWorld tw;
      private final StructurePopulator pop;

      public StructureLocatorKey(MegaChunk mc, TerraformWorld tw, StructurePopulator pop) {
         this.mc = mc;
         this.tw = tw;
         this.pop = pop;
      }

      public boolean equals(Object obj) {
         if (obj instanceof StructureLocator.StructureLocatorKey) {
            StructureLocator.StructureLocatorKey other = (StructureLocator.StructureLocatorKey)obj;
            if (other.mc.equals(this.mc) && other.tw.getName().equals(this.tw.getName())) {
               return this.pop.getClass().isInstance(other.pop);
            }
         }

         return false;
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.mc, this.tw, this.pop.getClass()});
      }
   }

   public static class StructureLocatorCacheLoader extends CacheLoader<StructureLocator.StructureLocatorKey, int[]> {
      @NotNull
      public int[] load(@NotNull StructureLocator.StructureLocatorKey key) {
         return null;
      }
   }
}
