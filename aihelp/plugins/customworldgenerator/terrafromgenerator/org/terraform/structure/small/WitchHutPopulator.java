package org.terraform.structure.small;

import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.NaturalSpawnType;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICAAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.SchematicParser;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.MultiMegaChunkStructurePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class WitchHutPopulator extends MultiMegaChunkStructurePopulator {
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
               if (height < TerraformGenerator.seaLevel) {
                  height = TerraformGenerator.seaLevel + GenUtils.randInt(random, 2, 3);
               } else {
                  height += GenUtils.randInt(random, 2, 3);
               }

               this.spawnSwampHut(tw, random, data, x, height, z);
            }
         }

      }
   }

   public void spawnSwampHut(TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      SimpleBlock core = new SimpleBlock(data, x, y, z);
      TerraformGeneratorPlugin.logger.info("Spawning Swamp Hut at " + core.getCoords());

      try {
         BlockFace face = BlockUtils.getDirectBlockFace(random);
         TerraSchematic swamphut = TerraSchematic.load("swamphut", core);
         swamphut.parser = new WitchHutPopulator.WitchHutSchematicParser(random, data);
         swamphut.setFace(face);
         swamphut.apply();
         Wall w = (new Wall(core.getDown(2), face)).getRear();
         w.getFront().getRight().downUntilSolid(random, new Material[]{Material.OAK_LOG});
         w.getFront().getLeft(2).downUntilSolid(random, new Material[]{Material.OAK_LOG});
         w.getRear(2).getRight().downUntilSolid(random, new Material[]{Material.OAK_LOG});
         w.getRear(2).getLeft(2).downUntilSolid(random, new Material[]{Material.OAK_LOG});
         x = w.getRear(2).get().getX();
         z = w.getRear(2).get().getZ();
         data.addEntity(x, y + 1, z, EntityType.WITCH);
         data.addEntity(x, y + 1, z, EntityType.CAT);
      } catch (FileNotFoundException var11) {
         TerraformGeneratorPlugin.logger.stackTrace(var11);
      }

      ((PopulatorDataICAAbstract)Objects.requireNonNull(TerraformGeneratorPlugin.injector.getICAData(data))).registerNaturalSpawns(NaturalSpawnType.WITCH, x - 3, y, z - 4, x + 3, y + 7, z + 4);
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 8242112), (int)(TConfig.c.STRUCTURES_SWAMPHUT_SPAWNRATIO * 10000.0D), 10000);
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
               } while(b == BiomeBank.SWAMP || b == BiomeBank.MANGROVE);

               return false;
            }
         }

         return false;
      }
   }

   public int[][] getCoordsFromMegaChunk(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      int num = TConfig.c.STRUCTURES_SWAMPHUT_COUNT_PER_MEGACHUNK;
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
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_SWAMPHUT_ENABLED && (TConfig.c.BIOME_SWAMP_WEIGHT > 0 || TConfig.c.BIOME_MANGROVE_WEIGHT > 0);
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(1211221L, chunkX, chunkZ);
   }

   public int getChunkBufferDistance() {
      return 1;
   }

   private static class WitchHutSchematicParser extends SchematicParser {
      private final Random rand;
      private final PopulatorDataAbstract pop;

      public WitchHutSchematicParser(Random rand, PopulatorDataAbstract pop) {
         this.rand = rand;
         this.pop = pop;
      }

      public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
         if (data.getMaterial().toString().contains("COBBLESTONE")) {
            data = Bukkit.createBlockData(StringUtils.replace(data.getAsString(), "cobblestone", ((Material)GenUtils.randChoice(this.rand, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE)).name().toLowerCase(Locale.ENGLISH)));
            super.applyData(block, data);
            if (GenUtils.chance(1, 5)) {
               BlockUtils.vineUp(block, 2);
            }
         } else if (data.getMaterial().toString().startsWith("OAK")) {
            super.applyData(block, data);
            if (data.getMaterial().toString().endsWith("LOG") && GenUtils.chance(1, 5)) {
               BlockUtils.vineUp(block, 2);
            }

            super.applyData(block, data);
         } else if (data.getMaterial() == Material.CHEST) {
            super.applyData(block, data);
            this.pop.lootTableChest(block.getX(), block.getY(), block.getZ(), TerraLootTable.VILLAGE_TEMPLE);
         } else {
            super.applyData(block, data);
         }

      }
   }
}
