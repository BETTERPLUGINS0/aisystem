package org.terraform.structure.small;

import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeClimate;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.SchematicParser;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.MultiMegaChunkStructurePopulator;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.V_1_20;
import org.terraform.utils.version.Version;

public class DesertWellPopulator extends MultiMegaChunkStructurePopulator {
   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         Random random = this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ());
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         int[][] var5 = this.getCoordsFromMegaChunk(tw, mc);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int[] coords = var5[var7];
            int x = coords[0];
            int z = coords[1];
            if (x >> 4 == data.getChunkX() && z >> 4 == data.getChunkZ()) {
               int height = GenUtils.getHighestGround(data, x, z);
               this.spawnDesertWell(tw, random, data, x, height, z, tw.getBiomeBank(x, z) == BiomeBank.BADLANDS);
            }
         }

      }
   }

   public void spawnDesertWell(TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z, boolean badlandsWell) {
      SimpleBlock core = new SimpleBlock(data, x, y, z);
      TerraformGeneratorPlugin.logger.info("Spawning Desert Well at " + core.getCoords());

      try {
         TerraSchematic desertWell = TerraSchematic.load("desert_well", core);
         desertWell.parser = new DesertWellPopulator.DesertWellSchematicParser(random, badlandsWell, y);
         desertWell.apply();
         core = core.getRelative(1, 0, 1);

         int depth;
         int i;
         for(depth = -3; depth <= 3; ++depth) {
            for(i = -3; i <= 3; ++i) {
               if (!badlandsWell) {
                  (new Wall(core.getRelative(depth, -1, i))).downLPillar(random, 10, new Material[]{Material.SANDSTONE, Material.CHISELED_SANDSTONE, Material.CUT_SANDSTONE, Material.SMOOTH_SANDSTONE});
               } else {
                  (new Wall(core.getRelative(depth, -1, i))).downLPillar(random, 10, new Material[]{Material.RED_SANDSTONE, Material.CHISELED_RED_SANDSTONE, Material.CUT_RED_SANDSTONE, Material.SMOOTH_RED_SANDSTONE});
               }
            }
         }

         depth = GenUtils.randInt(random, 5, 10);
         if (core.getUp().getType() != Material.WATER) {
            for(i = 0; i < depth; ++i) {
               if (i < depth - 3) {
                  core.getRelative(0, -i, 0).setType(Material.CAVE_AIR);
               } else {
                  core.getRelative(0, -i, 0).setType(Material.WATER);
               }
            }
         }
      } catch (FileNotFoundException var12) {
         TerraformGeneratorPlugin.logger.stackTrace(var12);
      }

   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 8291374), (int)(TConfig.c.STRUCTURES_DESERTWELL_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      if (!this.isEnabled()) {
         return false;
      } else {
         MegaChunk mc = new MegaChunk(chunkX, chunkZ);
         int[][] allCoords = this.getCoordsFromMegaChunk(tw, mc);
         int[][] var6 = allCoords;
         int var7 = allCoords.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            int[] coords = var6[var8];
            if (coords[0] >> 4 == chunkX && coords[1] >> 4 == chunkZ) {
               EnumSet<BiomeBank> biomes = GenUtils.getBiomesInChunk(tw, chunkX, chunkZ);
               Iterator var11 = biomes.iterator();

               BiomeBank b;
               do {
                  if (!var11.hasNext()) {
                     return this.rollSpawnRatio(tw, chunkX, chunkZ);
                  }

                  b = (BiomeBank)var11.next();
                  if (b.getClimate() != BiomeClimate.HOT_BARREN) {
                     return false;
                  }
               } while(b.getType() == BiomeType.FLAT);

               return false;
            }
         }

         return false;
      }
   }

   public int[][] getCoordsFromMegaChunk(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      int num = TConfig.c.STRUCTURES_DESERTWELL_COUNT_PER_MEGACHUNK;
      int[][] coords = new int[num][2];

      for(int i = 0; i < num; ++i) {
         coords[i] = mc.getRandomCoords(tw.getHashedRand((long)mc.getX(), mc.getZ(), 819227 * (1 + i)));
      }

      return coords;
   }

   public int[] getNearestFeature(@NotNull TerraformWorld tw, int rawX, int rawZ) {
      MegaChunk mc = new MegaChunk(rawX, 0, rawZ);
      double minDistanceSquared = 2.147483647E9D;
      int[] min = null;

      for(int nx = -1; nx <= 1; ++nx) {
         for(int nz = -1; nz <= 1; ++nz) {
            int[][] var10 = this.getCoordsFromMegaChunk(tw, mc);
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               int[] loc = var10[var12];
               double distSqr = Math.pow((double)(loc[0] - rawX), 2.0D) + Math.pow((double)(loc[1] - rawZ), 2.0D);
               if (distSqr < minDistanceSquared) {
                  minDistanceSquared = distSqr;
                  min = loc;
               }
            }
         }
      }

      return min;
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_DESERTWELL_ENABLED && (TConfig.c.BIOME_DESERT_WEIGHT > 0 || TConfig.c.BIOME_BADLANDS_WEIGHT > 0);
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(189821L, chunkX, chunkZ);
   }

   public int getChunkBufferDistance() {
      return 1;
   }

   private static class DesertWellSchematicParser extends SchematicParser {
      private final Random rand;
      private final boolean badlandsWell;
      private final int baseY;

      public DesertWellSchematicParser(Random rand, boolean badlandsWell, int baseY) {
         this.rand = rand;
         this.badlandsWell = badlandsWell;
         this.baseY = baseY;
      }

      public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
         if (this.badlandsWell) {
            data = Bukkit.createBlockData(StringUtils.replace(data.getAsString(), "sandstone", "red_sandstone"));
            if (data.getMaterial() == Material.RED_SANDSTONE && this.rand.nextInt(5) == 0) {
               data = Bukkit.createBlockData(Material.CHISELED_RED_SANDSTONE);
               super.applyData(block, data);
            } else {
               if (data.getMaterial() != Material.RED_SANDSTONE_STAIRS && data.getMaterial() != Material.RED_SANDSTONE_WALL && data.getMaterial().toString().contains("RED_SANDSTONE")) {
                  data = Bukkit.createBlockData(StringUtils.replace(data.getAsString(), "red_sandstone", ((Material)GenUtils.randChoice(this.rand, Material.RED_SANDSTONE, Material.SMOOTH_RED_SANDSTONE, Material.CUT_RED_SANDSTONE)).name().toLowerCase(Locale.ENGLISH)));
                  super.applyData(block, data);
               } else {
                  super.applyData(block, data);
               }

            }
         } else {
            if (data.getMaterial() == Material.SANDSTONE) {
               if (this.rand.nextInt(5) == 0) {
                  data = Bukkit.createBlockData(Material.CHISELED_SANDSTONE);
                  super.applyData(block, data);
                  return;
               }

               if (Version.VERSION.isAtLeast(Version.v1_20) && block.getY() == this.baseY && GenUtils.chance(this.rand, 1, 20)) {
                  data = Bukkit.createBlockData(V_1_20.SUSPICIOUS_SAND);
                  super.applyData(block, data);
                  block.getPopData().lootTableChest(block.getX(), block.getY(), block.getZ(), TerraLootTable.DESERT_WELL_ARCHAEOLOGY);
                  return;
               }
            }

            if (data.getMaterial() != Material.SANDSTONE_STAIRS && data.getMaterial() != Material.SANDSTONE_WALL && data.getMaterial().toString().contains("SANDSTONE")) {
               data = Bukkit.createBlockData(StringUtils.replace(data.getAsString(), "sandstone", ((Material)GenUtils.randChoice(this.rand, Material.SANDSTONE, Material.SMOOTH_SANDSTONE, Material.CUT_SANDSTONE)).name().toLowerCase(Locale.ENGLISH)));
               super.applyData(block, data);
            } else {
               super.applyData(block, data);
            }

         }
      }
   }
}
