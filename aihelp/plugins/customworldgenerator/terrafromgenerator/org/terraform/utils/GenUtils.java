package org.terraform.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeSection;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataSpigotAPI;
import org.terraform.data.CoordPair;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.datastructs.ConcurrentLRUCache;
import org.terraform.utils.noise.FastNoise;

public class GenUtils {
   public static final Random RANDOMIZER = new Random();
   private static final EnumSet<Material> BLACKLIST_HIGHEST_GROUND = EnumSet.noneOf(Material.class);
   public static ConcurrentLRUCache<ChunkCache, EnumSet<BiomeBank>> biomeQueryCache;

   public static void initGenUtils() {
      Material[] var0 = Material.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Material mat = var0[var2];
         if (mat.toString().contains("LEAVES") || mat.toString().contains("LOG") || mat.toString().contains("WOOD") || mat.toString().contains("MUSHROOM") || mat.toString().contains("FENCE") || mat.toString().contains("WALL") || mat.toString().contains("POTTED") || mat.toString().contains("BRICK") || mat.toString().contains("CHAIN") || mat.toString().contains("CORAL") || mat.toString().contains("POINTED_DRIPSTONE") || mat.toString().contains("NETHERRACK") || mat.toString().contains("MANGROVE") || mat == Material.HAY_BLOCK || mat == Material.ICE || mat == Material.CACTUS || mat == Material.BAMBOO || mat == Material.BAMBOO_SAPLING || mat == Material.IRON_BARS || mat == Material.LANTERN) {
            BLACKLIST_HIGHEST_GROUND.add(mat);
         }
      }

   }

   public static int getSign(@NotNull Random rand) {
      return rand.nextBoolean() ? 1 : -1;
   }

   @NotNull
   public static Collection<CoordPair> getCaveCeilFloors(PopulatorDataAbstract data, int x, int z, int minimumHeight) {
      int y = getHighestGround(data, x, z);
      int INVAL = TerraformGeneratorPlugin.injector.getMinY() - 1;
      int[] pair = new int[]{INVAL, INVAL};
      List<CoordPair> list = new ArrayList();

      for(int ny = y - 1; ny > TerraformGeneratorPlugin.injector.getMinY(); --ny) {
         Material type = data.getType(x, ny, z);
         if (BlockUtils.isStoneLike(type)) {
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

   public static int[] randomCoords(@NotNull Random rand, @NotNull int[] lowBound, @NotNull int[] highBound) {
      return new int[]{randInt(rand, lowBound[0], highBound[0]), randInt(rand, lowBound[1], highBound[1]), randInt(rand, lowBound[2], highBound[2])};
   }

   public static boolean chance(@NotNull Random rand, int chance, int outOf) {
      return randInt(rand, 1, outOf) <= chance;
   }

   public static boolean chance(int chance, int outOf) {
      return randInt(new Random(), 1, outOf) <= chance;
   }

   @NotNull
   public static EnumSet<BiomeBank> getBiomesInChunk(TerraformWorld tw, int chunkX, int chunkZ) {
      return (EnumSet)biomeQueryCache.get(new ChunkCache(tw, chunkX, chunkZ));
   }

   @Nullable
   public static Vector2f locateHeightDependentBiome(@NotNull TerraformWorld tw, @NotNull BiomeBank biome, @NotNull Vector2f center, int radius, int blockSkip) {
      if (!BiomeBank.isBiomeEnabled(biome)) {
         return null;
      } else if (tw.getBiomeBank(Math.round(center.x), Math.round(center.y)) == biome) {
         return new Vector2f(center.x, center.y);
      } else {
         int iter = 2;
         int x = (int)center.x;

         for(int z = (int)center.y; Math.abs(center.x - (float)x) < (float)radius || Math.abs(center.y - (float)z) < (float)radius; ++iter) {
            for(int i = 0; i < iter / 2; ++i) {
               switch(iter % 4) {
               case 0:
                  x += blockSkip;
                  break;
               case 1:
                  z -= blockSkip;
                  break;
               case 2:
                  x -= blockSkip;
                  break;
               case 3:
                  z += blockSkip;
               }
            }

            if (tw.getBiomeBank(x, z) == biome) {
               return new Vector2f((float)x, (float)z);
            }
         }

         return null;
      }
   }

   @Nullable
   public static Vector2f locateHeightIndependentBiome(TerraformWorld tw, @NotNull BiomeBank biome, @NotNull Vector2f centerBlockLocation) {
      if (!BiomeBank.isBiomeEnabled(biome)) {
         return null;
      } else {
         BiomeSection center = BiomeBank.getBiomeSectionFromBlockCoords(tw, (int)centerBlockLocation.x, (int)centerBlockLocation.y);
         int radius = 0;

         while(true) {
            for(Iterator var5 = center.getRelativeSurroundingSections(radius).iterator(); var5.hasNext(); ++radius) {
               BiomeSection sect = (BiomeSection)var5.next();
               if (sect.getBiomeBank() == biome) {
                  SimpleLocation sectionCenter = sect.getCenter();
                  return new Vector2f((float)sectionCenter.getX(), (float)sectionCenter.getZ());
               }
            }
         }
      }
   }

   public static Object weightedChoice(@NotNull Random rand, @NotNull Object... candidates) {
      if (candidates.length % 2 != 0) {
         throw new IllegalArgumentException();
      } else {
         ArrayList<Object> types = new ArrayList(50);

         for(int i = 0; i < candidates.length; i += 2) {
            Object type = candidates[i];
            int freq = (Integer)candidates[i + 1];

            for(int z = 0; z < freq; ++z) {
               types.add(type);
            }
         }

         return types.get(randInt(rand, 0, types.size() - 1));
      }
   }

   public static PlantBuilder weightedRandomSmallItem(@NotNull Random rand, @NotNull Object... candidates) {
      return (PlantBuilder)weightedChoice(rand, candidates);
   }

   public static Material weightedRandomMaterial(@NotNull Random rand, @NotNull Object... candidates) {
      return (Material)weightedChoice(rand, candidates);
   }

   @SafeVarargs
   public static <T> T randChoice(@NotNull Random rand, @NotNull T... candidates) {
      return candidates.length == 1 ? candidates[0] : candidates[randInt(rand, 0, candidates.length - 1)];
   }

   @SafeVarargs
   public static <T> T randChoice(T... candidates) {
      return randChoice(RANDOMIZER, candidates);
   }

   public static <T extends Enum<T>> T randChoice(@NotNull EnumSet<T> candidates) {
      int index = randInt(RANDOMIZER, 0, candidates.size() - 1);
      int i = 0;

      for(Iterator var3 = candidates.iterator(); var3.hasNext(); ++i) {
         T candidate = (Enum)var3.next();
         if (i == index) {
            return candidate;
         }
      }

      throw new IllegalArgumentException("EnumSet is empty");
   }

   public static int[] randomSurfaceCoordinates(@NotNull Random rand, @NotNull PopulatorDataAbstract data) {
      int chunkX = data.getChunkX();
      int chunkZ = data.getChunkZ();
      int x = randInt(rand, chunkX * 16, chunkX * 16 + 15);
      int z = randInt(rand, chunkZ * 16, chunkZ * 16 + 15);
      int y = getTrueHighestBlock(data, x, z);
      return new int[]{x, y, z};
   }

   public static int randInt(int min, int max) {
      return min == max ? min : randInt(RANDOMIZER, min, max);
   }

   public static int randInt(@NotNull Random rand, int d, int max) {
      if (d == max) {
         return d;
      } else {
         boolean negative = false;
         if (d < 0 && max < 0) {
            negative = true;
            d = -d;
            max = -max;
         }

         int randomNum;
         if (max < d) {
            randomNum = d;
            d = max;
            max = randomNum;
         }

         randomNum = rand.nextInt(max - d + 1) + d;
         return negative ? -randomNum : randomNum;
      }
   }

   public static int randOddInt(@NotNull Random rand, int min, int max) {
      int randomNum = rand.nextInt(max - min + 1) + min;
      if (randomNum % 2 == 0 && randomNum++ > max) {
         randomNum -= 2;
      }

      return randomNum;
   }

   public static double randDouble(@NotNull Random rand, double min, double max) {
      return rand.nextDouble() * (max - min) + min;
   }

   public static int getHighestX(@NotNull PopulatorDataAbstract data, int x, int z, Material X) {
      int y;
      for(y = TerraformGeneratorPlugin.injector.getMaxY() - 1; y > TerraformGeneratorPlugin.injector.getMinY() && data.getType(x, y, z) != X; --y) {
      }

      return y;
   }

   public static int getTrueHighestBlock(@NotNull PopulatorDataAbstract data, int x, int z) {
      int y;
      for(y = TerraformGeneratorPlugin.injector.getMaxY() - 1; y > TerraformGeneratorPlugin.injector.getMinY() && !data.getType(x, y, z).isSolid(); --y) {
      }

      return y;
   }

   public static int getHighestGroundOrSeaLevel(PopulatorDataAbstract data, int x, int z) {
      int y = getHighestGround(data, x, z);
      return Math.max(y, TerraformGenerator.seaLevel);
   }

   public static int getTrueHighestBlockBelow(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      while(y > TerraformGeneratorPlugin.injector.getMinY() && !data.getType(x, y, z).isSolid()) {
         --y;
      }

      return y;
   }

   @NotNull
   public static SimpleBlock getTrueHighestBlockBelow(@NotNull SimpleBlock block) {
      int y;
      for(y = block.getY(); y > TerraformGeneratorPlugin.injector.getMinY() && !block.getPopData().getType(block.getX(), y, block.getZ()).isSolid(); --y) {
      }

      return new SimpleBlock(block.getPopData(), block.getX(), y, block.getZ());
   }

   public static boolean isGroundLike(@NotNull Material mat) {
      if (BlockUtils.isStoneLike(mat) && mat != Material.PACKED_ICE && mat != Material.BLUE_ICE) {
         return true;
      } else if (mat != Material.SAND && mat != Material.RED_SAND && mat != Material.GRAVEL) {
         if (mat.isSolid()) {
            if (mat.isInteractable()) {
               return false;
            } else if (Tag.SLABS.isTagged(mat)) {
               return false;
            } else {
               return !BLACKLIST_HIGHEST_GROUND.contains(mat);
            }
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public static int getTransformedHeight(@NotNull TerraformWorld tw, int rawX, int rawZ) {
      ChunkCache cache = TerraformGenerator.getCache(tw, rawX >> 4, rawZ >> 4);
      int cachedY = cache.getTransformedHeight(rawX & 15, rawZ & 15);
      if (cachedY == TerraformGeneratorPlugin.injector.getMinY() - 1) {
         TerraformGenerator.buildFilledCache(tw, rawX >> 4, rawZ >> 4, cache);
         cachedY = cache.getTransformedHeight(rawX & 15, rawZ & 15);
      }

      return cachedY;
   }

   public static int getHighestGround(PopulatorDataAbstract data, int x, int z) {
      if (data instanceof PopulatorDataSpigotAPI) {
         return getTransformedHeight(data.getTerraformWorld(), x, z);
      } else {
         int y = TerraformGeneratorPlugin.injector.getMaxY() - 1;
         ChunkCache cache = TerraformGenerator.getCache(data.getTerraformWorld(), x >> 4, z >> 4);
         int cachedY = cache.getHighestGround(x, z);
         if (cachedY != TerraformGeneratorPlugin.injector.getMinY() - 1 && isGroundLike(data.getType(x, cachedY, z)) && !isGroundLike(data.getType(x, cachedY + 1, z))) {
            return cache.getHighestGround(x, z);
         } else {
            while(y > TerraformGeneratorPlugin.injector.getMinY()) {
               Material block = data.getType(x, y, z);
               if (isGroundLike(block)) {
                  break;
               }

               --y;
            }

            if (y <= TerraformGeneratorPlugin.injector.getMinY()) {
               TLogger var10000 = TerraformGeneratorPlugin.logger;
               int var10001 = TerraformGeneratorPlugin.injector.getMinY();
               var10000.error("GetHighestGround returned less than " + var10001 + "! (" + y + ")");

               try {
                  int var10002 = TerraformGeneratorPlugin.injector.getMinY();
                  throw new Exception("GetHighestGround returned less than " + var10002 + "! (" + y + ")");
               } catch (Exception var7) {
                  TerraformGeneratorPlugin.logger.stackTrace(var7);
               }
            }

            cache.cacheHighestGround(x, z, Integer.valueOf(y).shortValue());
            return y;
         }
      }
   }

   @NotNull
   public static Material[] mergeArr(@NotNull Material[]... arrs) {
      int totalLength = 0;
      int index = 0;
      Material[][] var3 = arrs;
      int var4 = arrs.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         Material[] arr = var3[var5];
         totalLength += arr.length;
      }

      Material[] res = new Material[totalLength];
      Material[][] var13 = arrs;
      var5 = arrs.length;

      for(int var14 = 0; var14 < var5; ++var14) {
         Material[] arr = var13[var14];
         Material[] var8 = arr;
         int var9 = arr.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Material mat = var8[var10];
            res[index++] = mat;
         }
      }

      return res;
   }

   @NotNull
   public static CoordPair[] vectorRandomObjectPositions(int seed, int chunkX, int chunkZ, int distanceBetween, float maxPerturbation) {
      FastNoise noise = new FastNoise(seed);
      noise.SetFrequency(1.0F);
      noise.SetGradientPerturbAmp(maxPerturbation);
      ArrayList<CoordPair> positions = new ArrayList();
      int startX = (chunkX << 4) - 5;
      int i = distanceBetween - startX % distanceBetween;
      startX += i != distanceBetween ? i : 0;
      int startZ = (chunkZ << 4) - 5;
      i = distanceBetween - startZ % distanceBetween;
      startZ += i != distanceBetween ? i : 0;

      for(int x = startX - distanceBetween; x < startX + 16 + distanceBetween; x += distanceBetween) {
         for(int z = startZ - distanceBetween; z < startZ + 16 + distanceBetween; z += distanceBetween) {
            Vector2f v = new Vector2f((float)x, (float)z);
            noise.GradientPerturb(v);
            v.x = (float)Math.round(v.x);
            v.y = (float)Math.round(v.y);
            if (v.x >= (float)(chunkX << 4) && v.x < (float)((chunkX << 4) + 16) && v.y >= (float)(chunkZ << 4) && v.y < (float)((chunkZ << 4) + 16)) {
               positions.add(new CoordPair((int)v.x, (int)v.y));
            }
         }
      }

      return (CoordPair[])positions.toArray(new CoordPair[0]);
   }

   @NotNull
   public static SimpleLocation[] randomObjectPositions(@NotNull TerraformWorld world, int chunkX, int chunkZ, int distanceBetween) {
      CoordPair[] vecs = vectorRandomObjectPositions((int)world.getSeed(), chunkX, chunkZ, distanceBetween, 0.35F * (float)distanceBetween);
      SimpleLocation[] locs = new SimpleLocation[vecs.length];

      for(int i = 0; i < vecs.length; ++i) {
         locs[i] = new SimpleLocation(vecs[i].x(), 0, vecs[i].z());
      }

      return locs;
   }

   @NotNull
   public static SimpleLocation[] randomObjectPositions(@NotNull TerraformWorld world, int chunkX, int chunkZ, int distanceBetween, float pertubMultiplier) {
      CoordPair[] vecs = vectorRandomObjectPositions((int)world.getSeed(), chunkX, chunkZ, distanceBetween, pertubMultiplier * (float)distanceBetween);
      SimpleLocation[] locs = new SimpleLocation[vecs.length];

      for(int i = 0; i < vecs.length; ++i) {
         locs[i] = new SimpleLocation(vecs[i].x(), 0, vecs[i].z());
      }

      return locs;
   }

   public static double randAngle(double base, double lowerBound, double upperBound) {
      return randDouble(new Random(), lowerBound * base, upperBound * base);
   }

   @NotNull
   public static <T> T choice(@NotNull Random rand, @NotNull T[] array) {
      if (array.length == 0) {
         throw new IllegalArgumentException("Provided array was length 0");
      } else {
         return array.length == 1 ? array[0] : array[rand.nextInt(array.length)];
      }
   }

   public static int getTripleChunk(int chunkCoord) {
      return chunkCoord >= 0 ? 1 + 3 * (chunkCoord / 3) : 1 + 3 * (-1 + (chunkCoord + 1) / 3);
   }
}
