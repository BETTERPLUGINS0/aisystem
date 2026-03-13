package org.terraform.structure.small.shipwreck;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.MultiMegaChunkStructurePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class ShipwreckPopulator extends MultiMegaChunkStructurePopulator {
   private static final String[] SCHEMATICS = new String[]{"upright-shipwreck-1", "tilted-shipwreck-1"};

   private static void dropDownBlock(@NotNull SimpleBlock block) {
      if (block.isSolid()) {
         Material type = block.getType();
         if (type == Material.CHEST) {
            return;
         }

         block.setType(Material.WATER);
         int depth = 0;

         while(!block.isSolid()) {
            block = block.getDown();
            ++depth;
            if (depth > 50) {
               return;
            }
         }

         block.getUp().setType(type);
      }

   }

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
               int height = GenUtils.getHighestGround(data, x, z) - 1 - random.nextInt(5);
               this.spawnShipwreck(tw, random, data, x, height + 1, z);
            }
         }

      }
   }

   public void spawnShipwreck(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      try {
         if (!BlockUtils.isWet((new SimpleBlock(data, x, 0, z)).getGround().getUp())) {
            y -= GenUtils.randInt(random, 4, 7);
         }

         y += GenUtils.randInt(random, -1, 1);
         TerraSchematic shipwreck = TerraSchematic.load(SCHEMATICS[random.nextInt(SCHEMATICS.length)], new SimpleBlock(data, x, y, z));
         shipwreck.parser = new ShipwreckSchematicParser(tw.getBiomeBank(x, z), random, data);
         shipwreck.setFace(BlockUtils.getDirectBlockFace(random));
         shipwreck.apply();
         TerraformGeneratorPlugin.logger.info("Spawning shipwreck at " + x + ", " + y + ", " + z + " with rotation of " + String.valueOf(shipwreck.getFace()));

         int i;
         int nx;
         int nz;
         int ny;
         for(i = 0; i < GenUtils.randInt(random, 0, 3); ++i) {
            nx = x + GenUtils.randInt(random, -8, 8);
            nz = z + GenUtils.randInt(random, -8, 8);
            ny = y + GenUtils.randInt(random, 0, 5);
            BlockUtils.replaceWaterSphere(nx * 7 * ny * 23 * nz, (float)GenUtils.randInt(1, 3), new SimpleBlock(data, nx, ny, nz));
         }

         for(i = 0; i < GenUtils.randInt(random, 5, 15); ++i) {
            nx = x + GenUtils.randInt(random, -8, 8);
            nz = z + GenUtils.randInt(random, -8, 8);
            ny = y + GenUtils.randInt(random, 0, 5);
            dropDownBlock(new SimpleBlock(data, nx, ny, nz));
         }

         data.addEntity(x, y + 12, z, EntityType.DROWNED);
         data.addEntity(x, y + 15, z, EntityType.DROWNED);
      } catch (Throwable var12) {
         TerraformGeneratorPlugin.logger.error("Something went wrong trying to place shipwreck at " + x + ", " + y + ", " + z);
         TerraformGeneratorPlugin.logger.stackTrace(var12);
      }

   }

   public int[][] getCoordsFromMegaChunk(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      int num = TConfig.c.STRUCTURES_SHIPWRECK_COUNT_PER_MEGACHUNK;
      int[][] coords = new int[num][2];

      for(int i = 0; i < num; ++i) {
         coords[i] = mc.getRandomCoords(tw.getHashedRand((long)mc.getX(), mc.getZ(), 191921 * (1 + i)));
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

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 12422), (int)(TConfig.c.STRUCTURES_SHIPWRECK_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      if (!this.isEnabled()) {
         return false;
      } else {
         MegaChunk mc = new MegaChunk(chunkX, chunkZ);
         int[][] var5 = this.getCoordsFromMegaChunk(tw, mc);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int[] coords = var5[var7];
            if (coords[0] >> 4 == chunkX && coords[1] >> 4 == chunkZ) {
               EnumSet<BiomeBank> biomes = GenUtils.getBiomesInChunk(tw, chunkX, chunkZ);
               double numWet = 0.0D;
               double numDry = 0.0D;
               Iterator var14 = biomes.iterator();

               while(var14.hasNext()) {
                  BiomeBank b = (BiomeBank)var14.next();
                  if (b.isDry()) {
                     ++numDry;
                  } else {
                     ++numWet;
                  }
               }

               return numWet / (numWet + numDry) > 0.5D && this.rollSpawnRatio(tw, chunkX, chunkZ);
            }
         }

         return false;
      }
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(221819019L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_SHIPWRECK_ENABLED;
   }

   public int getChunkBufferDistance() {
      return 1;
   }
}
