package org.terraform.structure.small.dungeon;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;

public class DrownedDungeonPopulator extends SmallDungeonPopulator {
   private static final Material[] cobbleSet;
   private static final Material[] graniteSet;
   private static final Material[] dioriteSet;
   private static final Material[] andesiteSet;
   private static final Material[] bricksSet;
   private static final Material[][] sets;

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
      int[] spawnCoords = new int[]{data.getChunkX() * 16, data.getChunkZ() * 16};
      int[][] allCoords = this.getCoordsFromMegaChunk(tw, mc);
      int[][] var6 = allCoords;
      int z = allCoords.length;

      for(int var8 = 0; var8 < z; ++var8) {
         int[] coords = var6[var8];
         if (coords[0] >> 4 == data.getChunkX() && coords[1] >> 4 == data.getChunkZ()) {
            spawnCoords = coords;
            break;
         }
      }

      int x = spawnCoords[0];
      z = spawnCoords[1];
      Random rand = this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ());
      this.spawnDungeonRoom(x, z, tw, rand, data);
   }

   public void spawnDungeonRoom(int x, int z, TerraformWorld tw, @NotNull Random rand, @NotNull PopulatorDataAbstract data) {
      TerraformGeneratorPlugin.logger.info("Spawning Drowned Dungeon at " + x + "," + z);
      int setIndex = rand.nextInt(sets.length);
      Material[] set = sets[setIndex];
      int radius = GenUtils.randInt(rand, 5, 10);

      int nx;
      int nz;
      int y;
      for(nx = -radius; nx <= radius; ++nx) {
         for(nz = -radius; nz <= radius; ++nz) {
            if (nx * nx + nz * nz <= radius * radius + GenUtils.randInt(rand, -10, 10)) {
               y = HeightMap.getBlockHeight(tw, x, z);
               if (nx == 0 && nz == 0) {
                  data.setSpawner(x, y + 1, z, EntityType.DROWNED);
               } else if (GenUtils.chance(rand, 1, 10)) {
                  Wall w = new Wall(new SimpleBlock(data, nx + x, y + 1, nz + z), BlockFace.NORTH);
                  w.LPillar(GenUtils.randInt(1, 7), rand, set);
               } else if (GenUtils.chance(rand, 1, 70)) {
                  Directional dir = (Directional)Bukkit.createBlockData(Material.CHEST);
                  dir.setFacing(BlockUtils.getDirectBlockFace(rand));
                  data.setBlockData(x + nx, y + 1, z + nz, dir);
                  if (radius < 7) {
                     data.lootTableChest(x + nx, y + 1, z + nz, TerraLootTable.UNDERWATER_RUIN_SMALL);
                  } else {
                     data.lootTableChest(x + nx, y + 1, z + nz, TerraLootTable.UNDERWATER_RUIN_BIG);
                  }
               } else if (GenUtils.chance(rand, 1, 10)) {
                  CoralGenerator.generateKelpGrowth(data, nx + x, y + 1, nz + z);
               } else if (GenUtils.chance(rand, 1, 10)) {
                  data.setType(x + nx, y, z + nz, Material.MAGMA_BLOCK);
               }
            }
         }
      }

      for(nx = -radius; nx <= radius; ++nx) {
         for(nz = -radius; nz <= radius; ++nz) {
            y = GenUtils.getHighestGround(data, nx + x, nz + z);
            if (GenUtils.chance(rand, 1, 15)) {
               CoralGenerator.generateSingleCoral(data, nx + x, y, nz + z);
            } else if (GenUtils.chance(rand, 1, 10)) {
               data.setType(x + nx, y + 1, z + nz, Material.SEAGRASS);
            }
         }
      }

   }

   static {
      cobbleSet = new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL};
      graniteSet = new Material[]{Material.GRANITE, Material.GRANITE_WALL};
      dioriteSet = new Material[]{Material.DIORITE, Material.DIORITE_WALL};
      andesiteSet = new Material[]{Material.ANDESITE, Material.ANDESITE_WALL};
      bricksSet = new Material[]{Material.STONE_BRICKS, Material.STONE_BRICK_WALL};
      sets = new Material[][]{cobbleSet, graniteSet, dioriteSet, andesiteSet, bricksSet};
   }
}
