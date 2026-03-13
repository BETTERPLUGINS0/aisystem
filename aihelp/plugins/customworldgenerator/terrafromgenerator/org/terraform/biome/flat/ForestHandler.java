package org.terraform.biome.flat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class ForestHandler extends BiomeHandler {
   protected static void spawnRock(@NotNull Random rand, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      ArrayList<int[]> locations = new ArrayList(20);
      locations.add(new int[]{x, y, z});
      locations.add(new int[]{x, y + 1, z});
      locations.add(new int[]{x + 1, y + 1, z});
      locations.add(new int[]{x - 1, y + 1, z});
      locations.add(new int[]{x, y + 1, z + 1});
      locations.add(new int[]{x, y + 1, z - 1});
      locations.add(new int[]{x + 1, y, z});
      locations.add(new int[]{x - 1, y, z});
      locations.add(new int[]{x, y, z + 1});
      locations.add(new int[]{x, y, z - 1});
      locations.add(new int[]{x + 1, y, z});
      locations.add(new int[]{x - 1, y, z + 1});
      locations.add(new int[]{x + 1, y, z + 1});
      locations.add(new int[]{x - 1, y, z - 1});
      locations.add(new int[]{x, y - 1, z});
      locations.add(new int[]{x + 1, y - 1, z});
      locations.add(new int[]{x - 1, y - 1, z});
      locations.add(new int[]{x, y - 1, z + 1});
      locations.add(new int[]{x, y - 1, z - 1});
      Iterator var6 = locations.iterator();

      while(true) {
         int Tx;
         int Ty;
         int Tz;
         do {
            if (!var6.hasNext()) {
               return;
            }

            int[] coords = (int[])var6.next();
            Tx = coords[0];
            Ty = coords[1];
            Tz = coords[2];
         } while(data.getType(Tx, Ty, Tz).isSolid() && !data.getType(Tx, Ty, Tz).toString().contains("LEAVES"));

         data.setType(Tx, Ty, Tz, (Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.STONE, Material.MOSSY_COBBLESTONE));
      }
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.FOREST;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{GenUtils.weightedRandomMaterial(rand, Material.GRASS_BLOCK, 35, Material.PODZOL, 3), Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      FastNoise pathNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_FOREST_PATHNOISE, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 12L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.07F);
         return n;
      });
      if ((double)pathNoise.GetNoise((float)rawX, (float)rawZ) > 0.3D && GenUtils.chance(random, 99, 100) && data.getBiome(rawX, rawZ) == this.getBiome() && BlockUtils.isDirtLike(data.getType(rawX, surfaceY, rawZ))) {
         data.setType(rawX, surfaceY, rawZ, Material.DIRT_PATH);
      }

      if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK && GenUtils.chance(random, 1, 10)) {
         switch(random.nextInt(4)) {
         case 0:
            PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
            break;
         case 1:
            PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
            break;
         case 2:
            BlockUtils.pickFlower().build(data, rawX, surfaceY + 1, rawZ);
            break;
         case 3:
            PlantBuilder.BUSH.build(data, rawX, surfaceY + 1, rawZ);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      int treeY;
      if (TConfig.c.TREES_FOREST_BIG_ENABLED && GenUtils.chance(random, 6, 10)) {
         int treeX = GenUtils.randInt(random, 2, 12) + data.getChunkX() * 16;
         int treeZ = GenUtils.randInt(random, 2, 12) + data.getChunkZ() * 16;
         if (data.getBiome(treeX, treeZ) == this.getBiome()) {
            treeY = GenUtils.getHighestGround(data, treeX, treeZ);
            if (BlockUtils.isDirtLike(data.getType(treeX, treeY, treeZ))) {
               FractalTypes.Tree.FOREST.build(tw, new SimpleBlock(data, treeX, treeY, treeZ));
            }
         }
      }

      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 8);
      SimpleLocation[] rocks = trees;
      treeY = trees.length;

      int var7;
      for(var7 = 0; var7 < treeY; ++var7) {
         SimpleLocation sLoc = rocks[var7];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            if (random.nextInt(7) == 0) {
               Wall w = new Wall(data, sLoc.getUp(), BlockUtils.getDirectBlockFace(random));
               int length = GenUtils.randInt(2, 3);

               for(int i = -length; i <= length && w.getFront(i).isAir() && w.getFront(i).getDown().isSolid(); ++i) {
                  w.getFront(i).setBlockData((new OrientableBuilder(Material.OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).get());
                  if (w.getFront(i).getUp().isAir() && random.nextInt(5) == 0) {
                     PlantBuilder.build(w.getFront(i).getUp(), PlantBuilder.RED_MUSHROOM, PlantBuilder.BROWN_MUSHROOM);
                  }
               }
            } else {
               FractalTypes.Tree.NORMAL_SMALL.build(tw, new SimpleBlock(data, sLoc.getX(), sLoc.getY(), sLoc.getZ()));
            }
         }
      }

      rocks = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 10);
      SimpleLocation[] var15 = rocks;
      var7 = rocks.length;

      for(int var16 = 0; var16 < var7; ++var16) {
         SimpleLocation sLoc = var15[var16];
         sLoc = sLoc.getAtY(GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ()));
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && (BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ())) || data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.COBBLESTONE || data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.MOSSY_COBBLESTONE || data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.STONE)) {
            int ny = GenUtils.randInt(random, -1, 1);
            spawnRock(random, data, sLoc.getX(), sLoc.getY() + ny, sLoc.getZ());
            if (GenUtils.chance(random, 1, 3)) {
               spawnRock(random, data, GenUtils.randInt(random, -1, 1) + sLoc.getX(), sLoc.getY() + ny + 1, sLoc.getZ() + GenUtils.randInt(random, -1, 1));
            }
         }
      }

   }
}
